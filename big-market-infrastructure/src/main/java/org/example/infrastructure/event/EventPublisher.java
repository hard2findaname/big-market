package org.example.infrastructure.event;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.types.event.BaseEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author atticus
 * @Date 2024/10/23 23:00
 * @description:
 */
@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void publish(String topic, BaseEvent.EventMessage<?> eventMessage){
        try {
            String messageJSON = JSON.toJSONString(eventMessage);
            rabbitTemplate.convertAndSend(topic, messageJSON);
            log.info("发送MQ消息 topic: {}, message:{}",topic,messageJSON);

        }catch (Exception e){
            log.error("发送MQ消息失败 topic: {}, message:{}",topic,JSON.toJSONString(eventMessage), e);
            throw e;
        }
    }
}
