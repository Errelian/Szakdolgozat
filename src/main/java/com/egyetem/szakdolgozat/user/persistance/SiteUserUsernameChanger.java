package com.egyetem.szakdolgozat.user.persistance;

public class SiteUserUsernameChanger {
    private Long userId;
    private String newName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
