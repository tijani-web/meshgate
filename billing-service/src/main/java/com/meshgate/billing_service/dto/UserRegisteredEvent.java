package com.meshgate.billing_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRegisteredEvent {
    private UUID authId;
    private String email;
    private String role;
    private LocalDateTime timestamp;

    public UserRegisteredEvent() {
    }

    public UserRegisteredEvent(UUID authId, String email, String role, LocalDateTime timestamp) {
        this.authId = authId;
        this.email = email;
        this.role = role;
        this.timestamp = timestamp;
    }

    public UUID getAuthId() {
        return authId;
    }

    public void setAuthId(UUID authId) {
        this.authId = authId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
