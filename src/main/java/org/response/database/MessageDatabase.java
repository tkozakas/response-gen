package org.response.database;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Tomas Kozakas
 */

public class MessageDatabase {
    private final Connection connection;

    @SneakyThrows
    public MessageDatabase() {
        Properties properties = new Properties();
        properties.load(new FileInputStream("database.properties"));

        String URL = properties.getProperty("URL");
        String USER = properties.getProperty("USER");
        String PASSWORD = properties.getProperty("PASSWORD");

        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @SneakyThrows
    public List<Message> getAll() {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM conversations");
        ResultSet resultSet = statement.executeQuery();

        List<Message> messageList = new ArrayList<>();
        while (resultSet.next()) {
            Message message = new Message(
                    resultSet.getString("user_message"),
                    resultSet.getString("chatbot_message"));
            messageList.add(message);
        }
        return messageList;
    }

    @SneakyThrows
    public void save(Message message) {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO conversations (user_message, chatbot_message) VALUES (?, ?)");
        statement.setString(1, message.getUserMessage());
        statement.setString(2, message.getChatbotMessage());
        statement.executeUpdate();
    }

}