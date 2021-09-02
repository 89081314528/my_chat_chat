package ru.gb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcApp {
    private static Connection connection;
    private static Statement stmt;

    public static void main(String[] args) {
        try {
            connect();
            createTableEx();
            readEx();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    static void createTableEx() throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_chat_chat (\n" +
                "        id    INTEGER PRIMARY KEY,\n" +
                "        login  TEXT,\n" +
                "        password TEXT,\n" +
                "        nickname TEXT\n" +
                "    );");
    }

    private static void dropTableEx() throws SQLException {
        stmt.executeUpdate("DROP TABLE IF EXISTS my_chat_chat;");
    }

    static List<SimpleAuthService.UserData> getDataFromTable() {
        List<SimpleAuthService.UserData> users = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM my_chat_chat;")) {
            while (rs.next()) {
                users.add(new SimpleAuthService.UserData(rs.getString(2),
                        rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException throwables) {
           throw new RuntimeException(throwables);
        }
        return users;
    }

    private static void readEx() throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM my_chat_chat;")) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
                        rs.getString(3) + " " + rs.getString(4));
            }
        }
    }

    private static void clearTableEx() throws SQLException {
        stmt.executeUpdate("DELETE FROM my_chat_chat;");
    }

    private static void deleteEx() throws SQLException {
        stmt.executeUpdate("DELETE FROM my_chat_chat WHERE login = 1;");
    }

    private static void updateEx() throws SQLException {
        stmt.executeUpdate("UPDATE my_chat_chat SET password = 1 WHERE login = 1;");
    }

    private static void insertEx(String name, Integer score) throws SQLException {
        stmt.executeUpdate(String.format("INSERT INTO my_chat_chat (login, password) VALUES ('%s', '%s')", name, score));
    }


    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:my_chat_chat.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

