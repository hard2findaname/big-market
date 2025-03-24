package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @Author atticus
 * @Date 2024/10/09 19:56
 * @description: api层抽奖奖品列表（请求对象）
 */
@Data
public class RaffleAwardListRequestDTO {

    // 抽奖策略ID
    @Deprecated
    private Long strategyId;
    //
    private Long activityId;
    //
    private String userId;
}
