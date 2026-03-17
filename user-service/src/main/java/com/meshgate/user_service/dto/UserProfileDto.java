package com.meshgate.user_service.dto;

import java.util.UUID;

public class UserProfileDto {
    private UUID id;
    private UUID authId;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;

    // Default constructor
    public UserProfileDto() {}

    // All args constructor
    public UserProfileDto(UUID id, UUID authId, String email, String firstName, String lastName, String avatarUrl) {
        this.id = id;
        this.authId = authId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAuthId() { return authId; }
    public void setAuthId(UUID authId) { this.authId = authId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private UUID authId;
        private String email;
        private String firstName;
        private String lastName;
        private String avatarUrl;

        Builder() {}

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder authId(UUID authId) { this.authId = authId; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder avatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; return this; }

        public UserProfileDto build() {
            return new UserProfileDto(id, authId, email, firstName, lastName, avatarUrl);
        }
    }
}