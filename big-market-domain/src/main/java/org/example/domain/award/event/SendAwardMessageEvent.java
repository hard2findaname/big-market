package org.example.domain.award.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/11/06 20:14
 * @description:
 */
@Service
public class SendAwardMessageEvent extends BaseEvent<SendAwardMessageEvent.AwardMessage> {

    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;

    @Override
    public EventMessage<AwardMessage> buildEventMessage(AwardMessage data) {
        return EventMessage.<AwardMessage>builder()
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
    public static class AwardMessage{
        /** 用户ID */
        private String userId;
        /** 活动ID */
        private Long activityId;
        /** 奖品标题（名称） */
        private String awardTitle;
    }

}
