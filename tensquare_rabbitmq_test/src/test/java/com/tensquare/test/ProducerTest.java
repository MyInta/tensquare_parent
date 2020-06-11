package com.tensquare.test;

import com.tensquare.rabbitmq.RabbitApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author inta
 * @date 2019/10/31
 * @describe
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitApplication.class)
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("test01", "直接测试队列");
    }

    //分裂模式
    @Test
    public void sendMsg2() {
        rabbitTemplate.convertAndSend("fanout_test","", "分裂模式");
    }
    //主题模式
    @Test
    public void sendMsg3() {
        rabbitTemplate.convertAndSend("topic_test","good.log", "主题模式");
    }
}
