package com.meshgate.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter; // Updated to Jackson 3 standard
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Standardized to match the Auth Service
    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String ROUTING_KEY = "user.registered.routing.key";
    
    // Notification-specific queue
    public static final String QUEUE_NAME = "notification-queue";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(userEventsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        // Modern replacement for the deprecated Jackson2JsonMessageConverter
        return new JacksonJsonMessageConverter();
    }
}