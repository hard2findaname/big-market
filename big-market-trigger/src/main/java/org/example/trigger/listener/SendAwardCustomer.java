package org.example.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author atticus
 * @Date 2024/11/07 15:07
 * @description:
 */
@Slf4j
@Component
public class SendAwardCustomer {
    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_award"))
    public void listener(String message){
        try {
            log.info("");
        }catch (Exception e){
            log.error("消费用户奖品发送消息失败。topic:{},message:{}",topic, message);
            throw e;
        }
    }
}
