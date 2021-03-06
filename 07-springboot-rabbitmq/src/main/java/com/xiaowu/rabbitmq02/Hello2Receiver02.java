package com.xiaowu.rabbitmq02;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "hello2")
public class Hello2Receiver02 {

    @RabbitHandler
    public void process(String message){
        System.out.println("Receiver2:"+message);
    }
}
