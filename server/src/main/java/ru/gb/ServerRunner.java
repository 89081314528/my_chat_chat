package ru.gb;

public class ServerRunner {

    public static void main(String[] args) {
        JdbcApp jdbcApp = new JdbcApp();
        try {
            jdbcApp.connect();
            jdbcApp.createTableEx();
            new ChatServer(jdbcApp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jdbcApp.disconnect();
        }
    }

}
