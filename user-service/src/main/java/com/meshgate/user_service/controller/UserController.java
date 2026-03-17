package com.meshgate.user_service.controller;

import com.meshgate.user_service.dto.UserProfileDto;
import com.meshgate.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // Explicit constructor instead of @RequiredArgsConstructor
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
public ResponseEntity<List<UserProfileDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
}

    @GetMapping("/{authId}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable UUID authId) {
        return ResponseEntity.ok(userService.getProfileByAuthId(authId));
    }

    @PutMapping("/{authId}")
    public ResponseEntity<UserProfileDto> updateProfile(@PathVariable UUID authId, @RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(authId, dto));
    }
}