package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @Author atticus
 * @Date 2025/04/11 13:46
 * @description: 抽奖规则信息-权重 请求体
 */
@Data
public class RaffleStrategyRuleWeightRequestDTO {
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
}
