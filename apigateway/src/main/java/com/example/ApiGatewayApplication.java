package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        System.out.println("ApiGateway");
        SpringApplication.run(ApiGatewayApplication.class);
    }
}