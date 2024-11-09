package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @Author atticus
 * @Date 2024/11/08 13:52
 * @description:
 */
@Data
public class ActivityDrawRequestDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
