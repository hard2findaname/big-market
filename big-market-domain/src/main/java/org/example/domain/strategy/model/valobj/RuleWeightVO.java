package org.example.domain.strategy.model.valobj;

import lombok.*;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/11 14:01
 * @description:
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleWeightVO {
    private String ruleValue;

    private Integer weight;

    private List<Integer> awardIds;

    private List<Award> awardList;
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Award{
        private Integer awardId;
        private String awardTitle;
    }
}
