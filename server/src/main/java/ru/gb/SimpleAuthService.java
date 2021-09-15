package ru.gb;

import java.util.List;

public class SimpleAuthService implements AuthService {

    static class UserData {
        private final String login;
        private final String password;
        private final String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }

    }

    public List<UserData> getUsers() {
        return users;
    }

    private List<UserData> users;

    public SimpleAuthService(JdbcApp jdbcApp)  {
               users = jdbcApp.getDataFromTable();
//                new ArrayList<>(); // ранее данные логин/пароль/ник заполнялись так
//        for (int i = 0; i < 5; i++) {
//            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
//        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData user : users) {
            if (login.equals(user.login) && password.equals(user.password)) {
                return user.nickname;
            }
        }
        return null;
    }
}
