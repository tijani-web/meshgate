package com.meshgate.auth_service.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
   
 */
public record UserRegisteredEvent(
    UUID eventId,
    UUID userId,
    String email,
    String firstName,
    String lastName,
    LocalDateTime timestamp
) {}