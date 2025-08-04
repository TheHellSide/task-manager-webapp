package com.example.to_do_list.User;

import com.example.to_do_list.Task.Task;
import java.util.*;

public class User {
    private long id;
    private String username;
    private String password;
    private final List<Task> tasks = new ArrayList<>();

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

    public List<Task> getTasks() {
        return tasks;
    }
}
