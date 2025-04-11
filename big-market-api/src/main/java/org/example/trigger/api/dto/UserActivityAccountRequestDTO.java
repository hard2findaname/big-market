package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @Author atticus
 * @Date 2025/04/11 10:29
 * @description: 用户活动账户
 */
@Data
public class UserActivityAccountRequestDTO {
    /** 用户ID */
    String userId;
    /** 活动ID */
    Long activityId;
}
