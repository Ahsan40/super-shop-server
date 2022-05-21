package app.main;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String passwords;
    private String mobile;
    private String type;

    public User(String name, String email, String mobile, String passwords, String type) {
        this.name = name;
        this.email = email;
        this.passwords = passwords;
        this.mobile = mobile;
        this.type = type;
    }

    public User (String email, String passwords) {
        this.email = email;
        this.passwords = passwords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
