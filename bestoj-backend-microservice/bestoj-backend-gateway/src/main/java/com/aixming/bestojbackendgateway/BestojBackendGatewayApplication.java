package com.aixming.bestojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author AixMing
 * @since 2025-03-24 20:07:35
 */
@SpringBootApplication
@EnableDiscoveryClient
public class BestojBackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BestojBackendGatewayApplication.class, args);
    }
}
