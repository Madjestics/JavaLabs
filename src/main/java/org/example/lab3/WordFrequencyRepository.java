package org.example.lab3;

import org.example.lab3.model.FileInfo;
import org.example.lab3.model.WordFrequencyInfo;
import org.example.lab3.model.WordStatisticInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WordFrequencyRepository {
    private final Connection connection;

    public WordFrequencyRepository(Connection connection) {
        this.connection = connection;
    }

    public UUID insertFile(String filePath, String filename)  {
        String insertSql = "INSERT INTO files (path, filename) VALUES (?, ?) RETURNING id";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

            insertStmt.setString(1, filePath);
            insertStmt.setString(2, filename);
            try (ResultSet rs = insertStmt.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString(FileInfo.FILE_ID_COLUMN));
                } else {
                    throw new RuntimeException("Вставка в таблицу файлов прошла неуспешно");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка записи в таблицу файлов", e);
        }
    }

    public void insertWordFrequency(WordFrequencyInfo wordFrequencyInfo) {
        String sql = "INSERT INTO word_frequency (file_id, word, count, percentage) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, wordFrequencyInfo.file_id());
            statement.setString(2, wordFrequencyInfo.word());
            statement.setInt(3, wordFrequencyInfo.count());
            statement.setDouble(4, wordFrequencyInfo.percentage());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка записи в таблицу статистики по словам: ", e);
        }
    }

    public FileInfo findFileById(UUID id) {
        String selectSql = "SELECT * FROM files WHERE id = ?";
        return findFile(id.toString(), selectSql);
    }

    public FileInfo findFileByPath(String filePath) {
        String selectSql = "SELECT * FROM files WHERE path = ?";
        return findFile(filePath, selectSql);
    }

    public FileInfo findFileByName(String filePath) {
        String selectSql = "SELECT * FROM files WHERE filename = ?";
        return findFile(filePath, selectSql);
    }

    private FileInfo findFile(String columnValue, String sql) {
        try (PreparedStatement selectStmt = connection.prepareStatement(sql)){

            selectStmt.setString(1, columnValue);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new FileInfo(UUID.fromString(rs.getString(FileInfo.FILE_ID_COLUMN)), rs.getString(FileInfo.FILE_PATH_COLUMN));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении информации о файле: ");
        }
        return null;
    }

    public List<WordStatisticInfo> findByWord(String word, int limit) throws SQLException {
        String sql = """
            SELECT f.filename, wf.word, wf.count, wf.percentage
              FROM word_frequency wf
              JOIN files f ON f.id = wf.file_id
             WHERE wf.word ILIKE ?
             ORDER BY wf.count DESC
             LIMIT ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, word+"%");
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<WordStatisticInfo> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new WordStatisticInfo(
                            rs.getString("filename"),
                            rs.getString("word"),
                            rs.getInt("count"),
                            rs.getDouble("percentage")
                    ));
                }
                return list;
            }
        }
    }
}
