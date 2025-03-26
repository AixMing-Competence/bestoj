package com.aixming.bestojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author AixMing
 * @since 2025-03-26 09:21:55
 */
@Slf4j
public class InitRabbitmq {

    public static void doInit() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("192.168.115.131");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            String exchange = "code_exchange";
            channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
            String queue = "code_queue";
            channel.queueDeclare(queue, true, false, false, null);
            log.info("消息队列启动成功");
            channel.queueBind(queue, exchange, "my_routingKey");
        } catch (IOException | TimeoutException e) {
            log.error("消息队列启动失败");
        }
    }

    public static void main(String[] args) {
        doInit();
    }
}
