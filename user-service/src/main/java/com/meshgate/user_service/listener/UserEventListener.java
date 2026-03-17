package com.meshgate.user_service.listener;

import com.meshgate.user_service.config.RabbitMQConfig;
import com.meshgate.user_service.dto.UserRegisteredEvent;
import com.meshgate.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    private final UserService userService;

    // Constructor injection (instead of @RequiredArgsConstructor)
    public UserEventListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event);
        try {
            userService.processUserRegistration(event);
        } catch (Exception e) {
            log.error("Error processing user registration event", e);
            throw e;
        }
    }
}