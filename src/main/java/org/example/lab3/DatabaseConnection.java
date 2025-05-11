package org.example.lab3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class DatabaseConnection {
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/local.properties")) {
            props.load(input);
        } catch (IOException e) {
            logger.log(new LogRecord(Level.SEVERE, "Ошибка загрузки properties файла"));
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.pass");
        return DriverManager.getConnection(url, user, pass);
    }
}
