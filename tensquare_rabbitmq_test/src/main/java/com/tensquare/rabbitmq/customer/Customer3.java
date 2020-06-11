package com.tensquare.rabbitmq.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author inta
 * @date 2019/11/1
 * @describe
 */
@Component
@RabbitListener(queues = "test03")
public class Customer3 {

    @RabbitHandler
    public void getMsg(String msg) {
        System.out.println("消费者3号的消息内容：" + msg);
    }

}
