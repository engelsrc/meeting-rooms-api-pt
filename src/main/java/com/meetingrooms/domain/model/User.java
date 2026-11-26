package com.meetingrooms.domain.model;

import java.util.Objects;

public class User {

    private Long id;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = Objects.requireNonNull(username, "username");
        this.password = Objects.requireNonNull(password, "password");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
