package org.example.domain.strategy.service.rule.filter;

import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @Author atticus
 * @Date 2024/09/29 23:53
 * @description: 抽象规则过滤接口
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}