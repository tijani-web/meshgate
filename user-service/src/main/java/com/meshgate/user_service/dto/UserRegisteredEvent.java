package com.meshgate.user_service.dto;

import java.util.UUID;
import java.time.LocalDateTime;

/**
 * It's immutable, concise
 */
public record UserRegisteredEvent(
        UUID eventId,
        UUID userId, // Mapped to authId
        String email,
        String firstName,
        String lastName,
        LocalDateTime timestamp) {
}