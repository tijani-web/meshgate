package com.meshgate.auth_service.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String email;

    // Constructor for backward compatibility (token only)
    public AuthResponse(String token) {
        this.token = token;
    }

    // New full constructor
    public AuthResponse(String token, UUID userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }

    // Default constructor for JSON deserialization
    public AuthResponse() {}

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}