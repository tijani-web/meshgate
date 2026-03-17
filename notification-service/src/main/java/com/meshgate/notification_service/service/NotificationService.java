package com.meshgate.notification_service.service;

import com.meshgate.notification_service.dto.UserRegisteredEvent;
import com.meshgate.notification_service.entity.Notification;
import com.meshgate.notification_service.entity.ProcessedEvent;
import com.meshgate.notification_service.repository.NotificationRepository;
import com.meshgate.notification_service.repository.ProcessedEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
            ProcessedEventRepository processedEventRepository,
            JavaMailSender javaMailSender) {
        this.notificationRepository = notificationRepository;
        this.processedEventRepository = processedEventRepository;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public void processUserRegisteredEvent(UserRegisteredEvent event) {
        UUID eventId = event.getAuthId(); // Using authId as eventId for user registration idempotency

        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Event already processed for user: " + eventId);
            return;
        }

        try {
            sendWelcomeEmail(event.getAuthId(), event.getEmail());
            saveNotification(event.getAuthId(), "EMAIL", "Welcome to MeshGate! Confirmation email sent.");

            ProcessedEvent processedEvent = new ProcessedEvent(eventId, LocalDateTime.now());
            processedEventRepository.save(processedEvent);
        } catch (Exception e) {
            saveNotification(event.getAuthId(), "EMAIL", "Failed to send welcome email.");
            System.err.println("Failed to process user registered event: " + e.getMessage());
            throw e; // Rethrow to let RabbitMQ handle retry if configured
        }
    }

    public void sendWelcomeEmail(UUID userId, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to MeshGate");
        message.setText("Thank you for registering to MeshGate, user " + userId.toString() + "!");

        javaMailSender.send(message);
        System.out.println("Email sent to: " + email);
    }

    public void saveNotification(UUID userId, String type, String message) {
        Notification notification = new Notification(userId, type, message, "SENT", LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
