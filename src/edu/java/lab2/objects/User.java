package edu.java.lab2.objects;

public class User {

    private String name;
    private String login;
    private String password;
    private String accessRight;
    private String regDate;

    public String getName() {
        return name;
    }
    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public String getRegDate() {
        return regDate;
    }
    public String getAccessRight() {
        return accessRight;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
