package com.example.task_manager_webapp.users.dto.register;

public class RegistrationRequest {
        private String email;
    private String username;
    private String password;

    public RegistrationRequest() {

    }

    public RegistrationRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
        }
}
