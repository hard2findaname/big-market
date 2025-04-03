package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.valobj.TaskStatusVO;
import org.example.types.event.BaseEvent;

/**
 * @Author atticus
 * @Date 2024/11/06 20:13
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /** 用户ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** */
    private String messageId;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendAwardMessageEvent.AwardMessage> message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStatusVO state;
}
