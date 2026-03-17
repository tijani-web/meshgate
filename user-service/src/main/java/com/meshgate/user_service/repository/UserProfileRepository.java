package com.meshgate.user_service.repository;

import com.meshgate.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByAuthId(UUID authId);
}
