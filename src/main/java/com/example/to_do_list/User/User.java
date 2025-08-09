package com.example.to_do_list.User;

import com.example.to_do_list.Task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    private long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;
    private String password;

    @JsonIgnore
    private boolean defaultTaskCreated;

    public User() {

    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.defaultTaskCreated = false;
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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDefaultTaskCreated() {
        return defaultTaskCreated;
    }

    public void setDefaultTaskCreated(boolean defaultTaskCreated) {
        this.defaultTaskCreated = defaultTaskCreated;
    }
}