package com.meshgate.billing_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter; // Spring 4.0 standard
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // These now match Auth-Service exactly
    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String ROUTING_KEY = "user.registered.routing.key";
    
    // Queue name is unique to the Billing Service
    public static final String QUEUE_NAME = "billing-queue";

    @Bean
    public Queue queue() {
        // Durable=true so the queue survives a RabbitMQ restart
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        // Binds the billing queue to the user exchange using the correct key
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Replaces the deprecated Jackson2JsonMessageConverter
        return new JacksonJsonMessageConverter();
    }
}