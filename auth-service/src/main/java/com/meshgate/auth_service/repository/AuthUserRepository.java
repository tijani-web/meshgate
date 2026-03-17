package com.meshgate.auth_service.repository;

import com.meshgate.auth_service.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
