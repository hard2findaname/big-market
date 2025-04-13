package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.entity.DispatchAwardEntity;
import org.example.domain.award.service.IAwardService;
import org.example.types.event.BaseEvent;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    @Resource
    private IAwardService awardService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award}"))
    public void listener(String message){
        try {
            log.info("消费用户奖品发送消息 topic:{},message:{}",topic, message);
            BaseEvent.EventMessage<SendAwardMessageEvent.AwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.AwardMessage>>() {
            }.getType());
            SendAwardMessageEvent.AwardMessage messageData = eventMessage.getData();

            //发放奖品
            DispatchAwardEntity dispatchAwardEntity = new DispatchAwardEntity();
            dispatchAwardEntity.setAwardId(messageData.getAwardId());
            dispatchAwardEntity.setAwardConfig(messageData.getAwardConfig());
            dispatchAwardEntity.setUserId(messageData.getUserId());
            dispatchAwardEntity.setOrderId(messageData.getOrderId());
            awardService.dispatchAward(dispatchAwardEntity);
        }catch (Exception e){
            log.error("消费用户奖品发送消息失败。topic:{},message:{}",topic, message);
            throw e;
        }
    }
}
