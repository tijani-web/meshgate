package com.meshgate.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route("user_route", r -> r.path("/api/v1/users/**")
                        .uri("lb://user-service"))
                .route("notification_route", r -> r.path("/api/v1/notifications/**")
                        .uri("lb://notification-service"))
                .route("billing_route", r -> r.path("/api/v1/billing/**")
                        .uri("lb://billing-service"))
                .build();
    }
}
