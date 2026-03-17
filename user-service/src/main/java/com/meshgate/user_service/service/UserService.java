package com.meshgate.user_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.meshgate.user_service.dto.UserProfileDto;
import com.meshgate.user_service.dto.UserRegisteredEvent;
import com.meshgate.user_service.entity.ProcessedEvent;
import com.meshgate.user_service.entity.UserProfile;
import com.meshgate.user_service.repository.ProcessedEventRepository;
import com.meshgate.user_service.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service  // Removed @RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final ProcessedEventRepository processedEventRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // Add explicit constructor for dependency injection
    public UserService(UserProfileRepository userProfileRepository, 
                      ProcessedEventRepository processedEventRepository) {
        this.userProfileRepository = userProfileRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void processUserRegistration(UserRegisteredEvent event) {
        // Use eventId() instead of getEventId()
        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Event {} already processed, skipping", event.eventId());
            return;
        }

        UserProfile profile = UserProfile.builder()
                .authId(event.userId()) // userId()
                .email(event.email()) // email()
                .firstName(event.firstName())// firstName()
                .lastName(event.lastName()) // lastName()
                .build();

        userProfileRepository.save(profile);

        // event.eventId() here too
        ProcessedEvent processedEvent = new ProcessedEvent(event.eventId(), LocalDateTime.now());
        processedEventRepository.save(processedEvent);

        log.info("Created user profile for auth id: {}", event.userId());
    }

    public UserProfileDto getProfileByAuthId(UUID authId) {
        UserProfile profile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("User profile not found for authId: " + authId));
        return mapToDto(profile);
    }

    public List<UserProfileDto> getAllUsers() {
    List<UserProfile> profiles = userProfileRepository.findAll();
    return profiles.stream()
            .map(this::mapToDto)
            .toList();
}

    @Transactional
    public UserProfileDto updateProfile(UUID authId, UserProfileDto dto) {
        UserProfile profile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new RuntimeException("User profile not found for authId: " + authId));

        if (dto.getFirstName() != null)
            profile.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            profile.setLastName(dto.getLastName());
        if (dto.getAvatarUrl() != null)
            profile.setAvatarUrl(dto.getAvatarUrl());

        userProfileRepository.save(profile);
        return mapToDto(profile);
    }

    private UserProfileDto mapToDto(UserProfile profile) {
        return UserProfileDto.builder()
                .id(profile.getId())
                .authId(profile.getAuthId())
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .avatarUrl(profile.getAvatarUrl())
                .build();
    }
}