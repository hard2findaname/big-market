package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2024/09/29 23:49
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyConditionEntity {
    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Integer strategyId;

}
