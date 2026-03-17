package com.meshgate.user_service.seed;

import com.meshgate.user_service.entity.UserProfile;
import com.meshgate.user_service.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserProfileRepository userProfileRepository;

    public DataSeeder(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void run(String... args) {
        if (userProfileRepository.count() > 0) {
            System.out.println("[Seed] User profiles already exist, skipping seed.");
            return;
        }

        System.out.println("[Seed] Seeding user profiles...");

        String[][] users = {
                // authId, email, firstName, lastName, avatarUrl
                { "00000000-0000-0000-0000-000000000001", "admin@meshgate.io", "System", "Admin",
                        "https://api.dicebear.com/7.x/initials/svg?seed=SA" },
                { "00000000-0000-0000-0000-000000000002", "john.doe@meshgate.io", "John", "Doe",
                        "https://api.dicebear.com/7.x/initials/svg?seed=JD" },
                { "00000000-0000-0000-0000-000000000003", "jane.smith@meshgate.io", "Jane", "Smith",
                        "https://api.dicebear.com/7.x/initials/svg?seed=JS" },
                { "00000000-0000-0000-0000-000000000004", "mike.chen@meshgate.io", "Mike", "Chen",
                        "https://api.dicebear.com/7.x/initials/svg?seed=MC" },
                { "00000000-0000-0000-0000-000000000005", "sarah.jones@meshgate.io", "Sarah", "Jones",
                        "https://api.dicebear.com/7.x/initials/svg?seed=SJ" },
                { "00000000-0000-0000-0000-000000000006", "alex.kumar@meshgate.io", "Alex", "Kumar",
                        "https://api.dicebear.com/7.x/initials/svg?seed=AK" },
                { "00000000-0000-0000-0000-000000000007", "emily.davis@meshgate.io", "Emily", "Davis",
                        "https://api.dicebear.com/7.x/initials/svg?seed=ED" },
                { "00000000-0000-0000-0000-000000000008", "david.wilson@meshgate.io", "David", "Wilson",
                        "https://api.dicebear.com/7.x/initials/svg?seed=DW" },
                { "00000000-0000-0000-0000-000000000009", "lisa.brown@meshgate.io", "Lisa", "Brown",
                        "https://api.dicebear.com/7.x/initials/svg?seed=LB" },
                { "00000000-0000-0000-0000-000000000010", "tom.garcia@meshgate.io", "Tom", "Garcia",
                        "https://api.dicebear.com/7.x/initials/svg?seed=TG" },
        };

        for (String[] u : users) {
            UserProfile profile = UserProfile.builder()
                    .authId(UUID.fromString(u[0]))
                    .email(u[1])
                    .firstName(u[2])
                    .lastName(u[3])
                    .avatarUrl(u[4])
                    .build();
            userProfileRepository.save(profile);
        }

        System.out.println("[Seed] Seeded 10 user profiles.");
    }
}
