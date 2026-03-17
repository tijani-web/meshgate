package com.meshgate.auth_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter; // Note the name change
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String ROUTING_KEY = "user.registered.routing.key";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Use JacksonJsonMessageConverter instead of Jackson2JsonMessageConverter
        return new JacksonJsonMessageConverter();
    }
}