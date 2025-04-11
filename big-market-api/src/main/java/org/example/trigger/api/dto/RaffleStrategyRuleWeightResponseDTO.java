package org.example.trigger.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/11 13:47
 * @description: 抽奖规则信息-权重 返回
 */
@Data
public class RaffleStrategyRuleWeightResponseDTO {
    private Integer ruleWeightCount;
    private Integer userActivityAccountTotalUse;
    private List<strategyAward> strategyAwardList;
    @Data
    public static class strategyAward{
        private Integer awardId;

        private String awardTitle;
    }
}
