package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @Author atticus
 * @Date 2024/10/02 19:59
 * @description: 抽奖策略规则规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModel;
}
