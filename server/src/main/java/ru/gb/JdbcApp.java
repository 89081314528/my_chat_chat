package ru.gb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcApp {
    private Connection connection;
    private Statement stmt;

    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner();
        JdbcApp jdbcApp = new JdbcApp(); // сделать все методы нестатическими
        try {
            jdbcApp.connect();
            jdbcApp.createTableEx();
//            jdbcApp.deleteEx("login5");
            jdbcApp.updateEx("password6","login5");
            jdbcApp.readEx();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jdbcApp.disconnect();
        }
    }

    void createTableEx() throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS my_chat_chat (\n" +
                "        id    INTEGER PRIMARY KEY,\n" +
                "        login  TEXT,\n" +
                "        password TEXT,\n" +
                "        nickname TEXT\n" +
                "    );");
    }

    private void dropTableEx() throws SQLException {
        stmt.executeUpdate("DROP TABLE IF EXISTS my_chat_chat;");
    }

    List<SimpleAuthService.UserData> getDataFromTable() {
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

    private void readEx() throws SQLException {
        try (ResultSet rs = stmt.executeQuery("SELECT * FROM my_chat_chat;")) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " +
                        rs.getString(3) + " " + rs.getString(4));
            }
        }
    }

    private void clearTableEx() throws SQLException {
        stmt.executeUpdate("DELETE FROM my_chat_chat;");
    }

    private void deleteEx(String login) throws SQLException {
        try (PreparedStatement ps =
                     connection.prepareStatement("DELETE FROM my_chat_chat WHERE login = ?")) {
            ps.setString(1, login);
            ps.executeUpdate();
        }
    }

    private void updateEx(String pass, String log) throws SQLException {
        try (PreparedStatement ps =
                     connection.prepareStatement("UPDATE my_chat_chat SET password = ? WHERE login = ?")) {
            ps.setString(1, pass);
            ps.setString(2, log);
            ps.executeUpdate();
        }
    }

    private void insertEx(String login, String password) throws SQLException {
        try (PreparedStatement ps =
                     connection.prepareStatement("INSERT INTO my_chat_chat (login, password) VALUES (?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.executeUpdate();
        }
    }


    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:my_chat_chat.db");
        stmt = connection.createStatement();
    }

    public void disconnect() {
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

