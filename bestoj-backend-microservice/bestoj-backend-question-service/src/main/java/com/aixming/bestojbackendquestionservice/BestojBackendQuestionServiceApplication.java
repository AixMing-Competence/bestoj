package com.aixming.bestojbackendquestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author AixMing
 * @since 2025-03-24 20:21:53
 */
@SpringBootApplication(scanBasePackages = "com.aixming")
@EnableDiscoveryClient
@EnableFeignClients({"com.aixming.bestojbackendserviceclient.service"})
public class BestojBackendQuestionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BestojBackendQuestionServiceApplication.class, args);
    }
}
