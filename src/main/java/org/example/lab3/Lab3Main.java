package org.example.lab3;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Lab3Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            return;
        }
        String dirPath = args[0];
        try {
            Connection connection = DatabaseConnection.getConnection();
            WordFrequencyRepository wordFrequencyRepository = new WordFrequencyRepository(connection);
            WordFrequencyProcessor wordFrequencyProcessor = new WordFrequencyProcessor(wordFrequencyRepository);

            wordFrequencyProcessor.processDirectory(dirPath);
            System.out.println("Обработка директории и запись данных в базу успешно завершены");
        } catch (SQLException | IOException e) {
            System.err.println("Ошибка при обработке: " + e.getMessage());
        }
    }
}
