package com.example.task_manager_webapp.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;
    private String hashed_password;

    @JsonIgnore
    private boolean defaultTaskCreated;

    public User() {

    }

    public User(String email, String username, String hashed_password) {
        if (email == null || username == null || hashed_password == null)
            throw new IllegalArgumentException("All fields are required.");

        this.email = email;
        this.username = username;
        this.hashed_password = hashed_password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return hashed_password;
    }

    public void setPassword(String hashed_password) {
        this.hashed_password = hashed_password;
    }
}