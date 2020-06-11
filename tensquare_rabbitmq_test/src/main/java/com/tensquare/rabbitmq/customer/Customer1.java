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
@RabbitListener(queues = "test01")
public class Customer1 {

    @RabbitHandler
    public void getMsg(String msg) {
        System.out.println("消费者1号或者消息内容：" + msg);
    }

}
