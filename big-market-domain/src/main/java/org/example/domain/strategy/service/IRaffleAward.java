package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/10/09 20:20
 * @description: 抽奖奖品查询
 */
public interface IRaffleAward {
    /**
     * 根据策略Id查询抽奖策略奖品列表
     *
     * @param: strategyId
     * @return: List<StrategyAwardEntity>
     **/
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
    /**
     * 根据活动Id查询抽奖策略奖品列表
     *
     * @param: activityId 活动ID
     * @return: List<StrategyAwardEntity>
     **/
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);
}
