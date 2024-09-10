package com.example.gateway.config;

import com.example.gateway.filter.AdminFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AdminFilter adminFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("module-common", r -> r.path("/module-common/**")
                        .uri("lb://MODULE-COMMON"))
                .route("module-admin", r -> r.path("/module-admin/**")
                        .filters(f -> f.filter(adminFilter))
                        .uri("lb://MODULE-ADMIN"))
                .build();
    }
}
