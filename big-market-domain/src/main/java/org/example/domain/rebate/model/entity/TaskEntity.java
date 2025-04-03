package org.example.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.valobj.TaskStatusVO;
import org.example.types.event.BaseEvent;

/**
 * @Author atticus
 * @Date 2025/04/02 21:44
 * @description: 行为返利任务实体
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
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStatusVO state;
}
