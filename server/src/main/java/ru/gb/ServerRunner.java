package ru.gb;

public class ServerRunner {

    public static void main(String[] args) {
        try {
            JdbcApp.connect();
            JdbcApp.createTableEx();
            new ChatServer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcApp.disconnect();
        }
    }

}
