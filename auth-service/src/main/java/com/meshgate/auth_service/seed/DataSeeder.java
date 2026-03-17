package com.meshgate.auth_service.seed;

import com.meshgate.auth_service.model.AuthUser;
import com.meshgate.auth_service.repository.AuthUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AuthUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AuthUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("[Seed] Auth users already exist, skipping seed.");
            return;
        }

        System.out.println("[Seed] Seeding auth users...");

        String[][] users = {
                { "00000000-0000-0000-0000-000000000001", "admin@meshgate.io", "Admin@123", "ADMIN" },
                { "00000000-0000-0000-0000-000000000002", "john.doe@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000003", "jane.smith@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000004", "mike.chen@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000005", "sarah.jones@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000006", "alex.kumar@meshgate.io", "Pass@123", "ADMIN" },
                { "00000000-0000-0000-0000-000000000007", "emily.davis@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000008", "david.wilson@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000009", "lisa.brown@meshgate.io", "Pass@123", "USER" },
                { "00000000-0000-0000-0000-000000000010", "tom.garcia@meshgate.io", "Pass@123", "USER" },
        };

        for (int i = 0; i < users.length; i++) {
            AuthUser user = new AuthUser();
            user.setId(UUID.fromString(users[i][0]));
            user.setEmail(users[i][1]);
            user.setPasswordHash(passwordEncoder.encode(users[i][2]));
            user.setRole(users[i][3]);
            user.setCreatedAt(LocalDateTime.now().minusDays(30 - i));
            userRepository.save(user);
        }

        System.out.println("[Seed] Seeded 10 auth users.");
    }
}
