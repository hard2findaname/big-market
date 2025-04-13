package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2025/04/11 21:24
 * @description: 用户积分值奖励实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditAwardEntity {
    /** 用户ID */
    private String userId;

    private BigDecimal creditAmount;
}
