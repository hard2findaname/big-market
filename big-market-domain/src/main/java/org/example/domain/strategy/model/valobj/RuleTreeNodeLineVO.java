package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2024/10/03 18:33
 * @description: 规则树节点 -线对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeNodeLineVO {
    /** 规则树ID */
    private String treeId;
    /** 规则Key节点 From */
    private String ruleNodeFrom;
    /** 规则Key节点 To */
    private String ruleNodeTo;
    /** 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private RuleLimitTypeVO ruleLimitType;
    /** 限定值（到下个节点） */
    private RuleLogicCheckTypeVO ruleLimitValue;

}
