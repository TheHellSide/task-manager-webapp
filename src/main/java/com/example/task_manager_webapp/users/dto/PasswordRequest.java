package com.example.task_manager_webapp.users.dto;

public class PasswordRequest {
    private String password;
    private String replacementPassword;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getReplacementPassword() {
        return replacementPassword;
    }

    public void setReplacementPassword(String replacementPassword) {
        this.replacementPassword = replacementPassword;
    }
}