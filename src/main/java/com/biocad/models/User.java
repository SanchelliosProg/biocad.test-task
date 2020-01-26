package com.biocad.models;

import lombok.Data;

import java.util.Map;

@Data
public final class User {
    private final String username;
    private final String domain;
    private final String password;

    public User(Map<String, String> map) {
        this.username = map.get("username");
        this.domain = map.get("domain");
        this.password = map.get("password");
    }

    public String getEmail() {
        return username + domain;
    }
}
