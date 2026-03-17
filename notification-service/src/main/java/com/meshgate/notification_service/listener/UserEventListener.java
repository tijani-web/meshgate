package com.meshgate.notification_service.listener;

import com.meshgate.notification_service.config.RabbitMQConfig;
import com.meshgate.notification_service.dto.UserRegisteredEvent;
import com.meshgate.notification_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private final NotificationService notificationService;

    @Autowired
    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onUserRegisteredEvent(UserRegisteredEvent event) {
        System.out.println("Received user.registered event for user: " + event.getAuthId());
        notificationService.processUserRegisteredEvent(event);
    }
}
