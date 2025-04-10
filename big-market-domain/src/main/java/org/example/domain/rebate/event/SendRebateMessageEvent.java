package org.example.domain.rebate.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2025/04/02 21:47
 * @description: 发送返利消息事件
 */
@Component
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.RebateMessage> {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Override
    public EventMessage<RebateMessage> buildEventMessage(RebateMessage data) {
        return EventMessage.<RebateMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timeStamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RebateMessage{
        /** 用户ID */
        private String userId;
        /** 返利描述 */
        private String rebateDesc;
        /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
        private String rebateType;
        /** 返利配置【sku值，积分值】 */
        private String rebateConfig;
        /** 业务ID - 拼接的唯一值 */
        private String bizId;
    }
}
