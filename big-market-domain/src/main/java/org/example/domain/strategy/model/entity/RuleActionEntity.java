package org.example.domain.strategy.model.entity;

import lombok.*;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;

/**
 * @Author atticus
 * @Date 2024/10/01 15:40
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity>{
    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;



    static public class RaffleEntity{


    }
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖。
         */
        private String ruleWeightValueKey;

        /**
         * 奖品ID；
         */
        private Integer awardId;
    }

    static public class RaffleWhileEntity extends RaffleEntity {}

    static public class RaffleAfterEntity extends RaffleEntity {}

}
