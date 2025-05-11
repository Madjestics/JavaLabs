package org.example.lab3;

import org.example.general.WordFrequencyCounter;
import org.example.general.WordFrequencyResult;
import org.example.lab3.model.FileInfo;
import org.example.lab3.model.WordFrequencyInfo;
import org.example.lab3.model.WordStatisticInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class WordFrequencyProcessor {
    private final WordFrequencyRepository wordFrequencyRepository;
    private static final Logger LOGGER = Logger.getLogger(WordFrequencyCounter.class.getName());

    public static WordFrequencyProcessor defaultProcessor() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            WordFrequencyRepository wordFrequencyRepository = new WordFrequencyRepository(connection);
            return new WordFrequencyProcessor(wordFrequencyRepository);
        }
        catch (SQLException e) {
            LOGGER.log(new LogRecord(Level.SEVERE, "Произошла ошибка при попытке подключения к БД: " + e.getMessage()));
        }
        return null;
    }

    public WordFrequencyProcessor(WordFrequencyRepository wordFrequencyRepository) {
        this.wordFrequencyRepository = wordFrequencyRepository;
    }

    public void processDirectory(String dirPath) throws IOException {
        try (Stream<Path> pathStream = Files.list(Path.of(dirPath))) {
            pathStream.parallel()
                    .forEach(path -> {
                        WordFrequencyResult result = WordFrequencyCounter.countWordFrequency(path);
                        processFile(path, result.wordFrequency(), result.totalWords());
                    });
        }
    }

    public List<WordStatisticInfo> searchByWord(String word, int limit) throws SQLException {
        return wordFrequencyRepository.findByWord(word, limit);
    }

    public File openFile(String filename) throws IOException {
        String path = getFilePath(filename);
        if (path == null) {
            return null;
        }
        File file = new File(path);
        return (file.exists() && file.isFile()) ? file : null;
    }

    private String getFilePath(String filename) {
        FileInfo fi = wordFrequencyRepository.findFileByName(filename);
        return fi != null ? fi.path() : null;
    }

    private void processFile(Path file, Map<String, Integer> wordFrequencyMap, int totalWords) {
        FileInfo existedFile = wordFrequencyRepository.findFileByPath(file.toAbsolutePath().toString());

        UUID fileId = (existedFile == null) ? wordFrequencyRepository.insertFile(file.toAbsolutePath().toString(), file.getFileName().toString()) : existedFile.id();

        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            int count = entry.getValue();
            double percentage = totalWords > 0 ? (double) count / totalWords * 100 : 0.0;

            wordFrequencyRepository.insertWordFrequency(new WordFrequencyInfo(fileId, entry.getKey(), count, percentage));
        }
        LOGGER.log(new LogRecord(Level.INFO,  String.format("Загрузка слов из файла %s завешена", file.getFileName().toString())));
    }
}
