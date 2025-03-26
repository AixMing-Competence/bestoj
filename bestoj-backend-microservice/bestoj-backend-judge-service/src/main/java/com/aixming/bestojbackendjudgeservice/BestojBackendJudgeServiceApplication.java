package com.aixming.bestojbackendjudgeservice;

import com.aixming.bestojbackendjudgeservice.rabbitmq.InitRabbitmq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"com.aixming.bestojbackendserviceclient.service"})
public class BestojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        InitRabbitmq.doInit();
        SpringApplication.run(BestojBackendJudgeServiceApplication.class, args);
    }

}
