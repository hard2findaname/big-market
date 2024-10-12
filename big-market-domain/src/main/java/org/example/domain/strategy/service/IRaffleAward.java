package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/10/09 20:20
 * @description: 抽奖奖品查询
 */
public interface IRaffleAward {

    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
