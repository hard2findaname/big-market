package org.example.infrastructure.persistent.po;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/10/30 17:44
 * @description: 任务表，用于发送MQ（补偿性）
 */
@Data
public class Task {
    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** */
    private String messageId;
    /** 消息主体 */
    private String message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
