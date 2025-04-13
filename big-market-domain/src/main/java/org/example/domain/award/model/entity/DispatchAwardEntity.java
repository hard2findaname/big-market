package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2025/04/11 21:09
 * @description: 分发奖品实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispatchAwardEntity {

    private String userId;
    private String orderId;
    private Integer awardId;
    private String awardConfig;
}
