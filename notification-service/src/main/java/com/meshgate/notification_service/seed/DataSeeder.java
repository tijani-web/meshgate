package com.meshgate.notification_service.seed;

import com.meshgate.notification_service.entity.Notification;
import com.meshgate.notification_service.repository.NotificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    public DataSeeder(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run(String... args) {
        if (notificationRepository.count() > 0) {
            System.out.println("[Seed] Notifications already exist, skipping seed.");
            return;
        }

        System.out.println("[Seed] Seeding notifications...");

        // userId, type, message, status, daysAgo
        Object[][] notifications = {
                { "00000000-0000-0000-0000-000000000001", "EMAIL", "Welcome to MeshGate! Your admin account is ready.",
                        "SENT", 30 },
                { "00000000-0000-0000-0000-000000000001", "IN_APP", "System maintenance scheduled for this weekend.",
                        "SENT", 5 },
                { "00000000-0000-0000-0000-000000000002", "EMAIL",
                        "Welcome to MeshGate! Your account has been created.", "SENT", 28 },
                { "00000000-0000-0000-0000-000000000002", "IN_APP", "Your subscription has been upgraded to PRO.",
                        "SENT", 20 },
                { "00000000-0000-0000-0000-000000000002", "EMAIL", "Invoice #INV-001 has been paid. Thank you!", "SENT",
                        15 },
                { "00000000-0000-0000-0000-000000000003", "EMAIL", "Welcome to MeshGate! Confirmation email sent.",
                        "SENT", 26 },
                { "00000000-0000-0000-0000-000000000003", "IN_APP", "Your PRO subscription is now active.", "SENT",
                        22 },
                { "00000000-0000-0000-0000-000000000004", "EMAIL",
                        "Welcome to MeshGate! Start exploring your dashboard.", "SENT", 24 },
                { "00000000-0000-0000-0000-000000000005", "EMAIL", "Welcome to MeshGate! Your free tier is active.",
                        "SENT", 22 },
                { "00000000-0000-0000-0000-000000000005", "IN_APP", "Upgrade to PRO for advanced features.", "SENT",
                        10 },
                { "00000000-0000-0000-0000-000000000006", "EMAIL",
                        "Welcome to MeshGate! Your enterprise account is ready.", "SENT", 20 },
                { "00000000-0000-0000-0000-000000000006", "IN_APP",
                        "New API keys have been generated for your account.", "SENT", 8 },
                { "00000000-0000-0000-0000-000000000006", "EMAIL", "Invoice #INV-006 payment confirmed.", "SENT", 5 },
                { "00000000-0000-0000-0000-000000000007", "EMAIL", "Welcome to MeshGate! Confirmation email sent.",
                        "SENT", 18 },
                { "00000000-0000-0000-0000-000000000007", "IN_APP",
                        "Payment overdue for Invoice #INV-010. Please update payment.", "SENT", 3 },
                { "00000000-0000-0000-0000-000000000008", "EMAIL",
                        "Welcome to MeshGate! Your account has been activated.", "SENT", 16 },
                { "00000000-0000-0000-0000-000000000009", "EMAIL", "Welcome to MeshGate! Start building today.", "SENT",
                        14 },
                { "00000000-0000-0000-0000-000000000009", "IN_APP", "Check out our new billing dashboard.", "SENT", 7 },
                { "00000000-0000-0000-0000-000000000010", "EMAIL", "Welcome to MeshGate! Your PRO trial has started.",
                        "SENT", 12 },
                { "00000000-0000-0000-0000-000000000010", "IN_APP", "Your PRO subscription has been renewed.", "SENT",
                        2 },
        };

        for (Object[] n : notifications) {
            Notification notification = new Notification(
                    UUID.fromString((String) n[0]),
                    (String) n[1],
                    (String) n[2],
                    (String) n[3],
                    LocalDateTime.now().minusDays((int) n[4]));
            notificationRepository.save(notification);
        }

        System.out.println("[Seed] Seeded 20 notifications.");
    }
}
