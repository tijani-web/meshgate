package com.meshgate.user_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter; // Use Jackson 3 standard
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Standardized names to match Auth-Service
    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String ROUTING_KEY = "user.registered.routing.key";
    
    // Unique queue for the User Service (Profile Creation)
    public static final String QUEUE_NAME = "user-profile-creation-queue";

    @Bean
    public Queue userProfileCreationQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue userProfileCreationQueue, TopicExchange userEventsExchange) {
        return BindingBuilder.bind(userProfileCreationQueue)
                .to(userEventsExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        // Modern replacement for the deprecated Jackson2JsonMessageConverter
        return new JacksonJsonMessageConverter();
    }
}