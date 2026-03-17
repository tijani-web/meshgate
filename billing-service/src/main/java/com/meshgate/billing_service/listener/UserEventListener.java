package com.meshgate.billing_service.listener;

import com.meshgate.billing_service.config.RabbitMQConfig;
import com.meshgate.billing_service.dto.UserRegisteredEvent;
import com.meshgate.billing_service.entity.ProcessedEvent;
import com.meshgate.billing_service.repository.ProcessedEventRepository;
import com.meshgate.billing_service.service.BillingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    
    private final BillingService billingService;
    private final ProcessedEventRepository processedEventRepository;

    public UserEventListener(BillingService billingService, ProcessedEventRepository processedEventRepository) {
        this.billingService = billingService;
        this.processedEventRepository = processedEventRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        // IDEMPOTENCY CHECK:
        // Use a clean ID. Since you're on Java 24, we use String.formatted or simple concat.
        String eventId = "REG-" + event.getAuthId(); 

        if (processedEventRepository.existsById(eventId)) {
            log.warn("Event already processed, skipping: {}", eventId);
            return;
        }

        try {
            log.info("Processing billing for new user: {}", event.getAuthId());
            
            billingService.processUserRegisteredEvent(event);
            
            // Mark as processed only AFTER successful billing logic
            processedEventRepository.save(new ProcessedEvent(eventId));
            
        } catch (Exception e) {
            log.error("Fatal error in billing listener: {}", e.getMessage());
            // Throwing allows RabbitMQ to use the DLQ (Dead Letter Queue) if configured
            throw e; 
        }
    }
}