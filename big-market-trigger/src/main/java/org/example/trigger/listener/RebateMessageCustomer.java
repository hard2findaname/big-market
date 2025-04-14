package org.example.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.SKURechargeEntity;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.credit.model.entity.TradeEntity;
import org.example.domain.credit.model.valobj.TradeNameVO;
import org.example.domain.credit.model.valobj.TradeTypeVO;
import org.example.domain.credit.service.IUserCreditAdjustService;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.valobj.RebateTypeVO;
import org.example.types.enums.ResponseCode;
import org.example.types.event.BaseEvent;
import org.example.types.exception.AppException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2025/04/03 15:04
 * @description: 监听行为返利消息
 */
@Slf4j
@Component
public class RebateMessageCustomer {

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private IUserCreditAdjustService creditAdjustService;
    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message){
        try {
            log.info("监听用户行为返利消息 topic:{} message:{}", topic, message);
            // 1. 转换对象
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {
            }.getType());

            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();

            switch (rebateMessage.getRebateType()){
                case "sku":
                    // 2. 入账奖励
                    SKURechargeEntity skuRechargeEntity = new SKURechargeEntity();
                    skuRechargeEntity.setUserId(rebateMessage.getUserId());
                    skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
                    skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
                    break;
                case "integral":
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setUserId(rebateMessage.getUserId());
                    tradeEntity.setTradeName(TradeNameVO.REBATE);
                    tradeEntity.setTradeType(TradeTypeVO.FORWARD);
                    tradeEntity.setAmount(new BigDecimal(rebateMessage.getRebateConfig()));
                    tradeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    creditAdjustService.createOrder(tradeEntity);
                    break;
            }


        }catch (AppException e) {
            log.error("监听用户行为返利消息， 消费失败 topic:{} message:{}, info:{}", topic, message, e.getInfo());
            return;
        } catch (Exception e){
            log.error("监听用户行为返利消息， 消费失败 topic:{} message:{}", topic, message, e);
            throw e;
        }
    }
}
