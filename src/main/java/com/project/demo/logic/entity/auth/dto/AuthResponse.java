package com.project.demo.logic.entity.auth.dto;

public class AuthResponse {
    private String token;
    private long expiresIn;
    private String email;
    private String name;
    private String role;

    public AuthResponse(String token, long expiresIn, String email, String name, String role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
