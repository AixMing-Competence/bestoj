package com.aixming.bestojbackenduserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author AixMing
 * @since 2025-03-24 20:29:06
 */
@SpringBootApplication(scanBasePackages = "com.aixming")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.aixming.bestojbackendserviceclient.service"})
public class BestojBackendUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BestojBackendUserServiceApplication.class, args);
    }
}
