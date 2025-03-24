package org.example.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2024/10/09 19:57
 * @description: api层抽奖奖品列表（应答对象）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardListResponseDTO {
    // 奖品ID
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 解锁奖品抽奖次数计数- 抽奖N次解锁，未配置为空
    private Integer awardUnlockCount;
    // 奖品是否解锁 - true 已解锁
    private boolean isAwardLocked;
    // 待解锁次数 - 为 规定N次 - 用户已抽奖次数
    private Integer unlockRemaining;
}
