package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.DeliveryOrderEntity;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.credit.event.CreditAdjustSuccessMessageEvent;
import org.example.domain.credit.service.IUserCreditAdjustService;
import org.example.types.enums.ResponseCode;
import org.example.types.event.BaseEvent;
import org.example.types.exception.AppException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2025/04/14 15:02
 * @description:
 */
@Slf4j
@Component
public class CreditAdjustSuccessCustomer {
    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @RabbitListener(queuesToDeclare = @Queue("${spring.rabbitmq.topic.credit_adjust_success}"))
    public void listen(String message) {
        try {
            log.info("监听积分账户调整成功消息，进行交易商品发货 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage eventMessageData = eventMessage.getData();

            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(eventMessageData.getUserId());
            deliveryOrderEntity.setOutBusinessNo(eventMessageData.getOutBusinessNo());
            raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);

        } catch (AppException e) {
            if (ResponseCode.INDEX_DUP.getCode().equals(e.getCode())) {
                log.warn("监听积分账户调整成功消息，进行交易商品发货，消费重复 topic: {} message: {}", topic, message, e);
                return;
            }
            throw e;
        } catch (Exception e) {
            log.error("监听积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", topic, message, e);
            throw e;
        }


    }

}
