package com.egyetem.szakdolgozat.database.user.persistance;

public class SiteUserRegisterer {

    private String username;

    private String password;

    private String eMail;

    public SiteUserRegisterer(String username, String password, String eMail) {
        this.username = username;
        this.password = password;
        this.eMail = eMail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
