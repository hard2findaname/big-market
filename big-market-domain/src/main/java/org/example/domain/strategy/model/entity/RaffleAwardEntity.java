package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2024/09/29 23:52
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {
    /** 奖品ID */
    private Integer awardId;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品标题（名称） */
    private String awardTitle;

    private Integer sort;


}
