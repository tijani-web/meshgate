package com.meshgate.auth_service.service;

import com.meshgate.auth_service.config.RabbitMQConfig;
import com.meshgate.auth_service.dto.AuthResponse;
import com.meshgate.auth_service.dto.LoginRequest;
import com.meshgate.auth_service.dto.RegisterRequest;
import com.meshgate.auth_service.event.UserRegisteredEvent;
import com.meshgate.auth_service.model.AuthUser;
import com.meshgate.auth_service.repository.AuthUserRepository;
import com.meshgate.auth_service.security.JwtUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RabbitTemplate rabbitTemplate;

    public AuthService(AuthUserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtil jwtUtil,
            UserDetailsService userDetailsService, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        AuthUser user = new AuthUser();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setCreatedAt(LocalDateTime.now());

        AuthUser savedUser = userRepository.save(user);

        // Create event with proper fields
        UserRegisteredEvent event = new UserRegisteredEvent(
                UUID.randomUUID(),           // eventId
                savedUser.getId(),           // userId (maps to authId in other services)
                savedUser.getEmail(),        // email
                request.getFirstName(),      // firstName
                request.getLastName(),       // lastName
                LocalDateTime.now()          // timestamp
        );

        // Send to RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);

        // Generate Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        // RETURN WITH USER ID AND EMAIL
        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        
        // Get the full user to get ID
        AuthUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtUtil.generateToken(userDetails);
        
        // RETURN WITH USER ID AND EMAIL
        return new AuthResponse(token, user.getId(), user.getEmail());
    }
}