package org.example.domain.strategy.service;

import org.example.domain.strategy.model.valobj.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 2025/03/24 13:05
 * @description: 抽奖规则接口: 对规则的业务功能查询
 */
public interface IRaffleRule {
    /**
     * 根据规则树Id集合查询奖品中加锁数量的配置【奖品有些需要抽奖N次才能解锁】
     * @param: treeIds                  规则树ID值
     * @return: Map<String,Integer>     加锁值
     **/
    Map<String, Integer> queryAwardRuleLockCount(String... treeIds);

    List<RuleWeightVO> queryAwardRuleWeight(Long StrategyId);

    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);
}
