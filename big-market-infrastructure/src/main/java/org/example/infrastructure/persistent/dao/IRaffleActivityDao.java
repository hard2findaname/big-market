package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;

/**
 * @Author atticus
 * @Date 2024/10/14 22:14
 * @description: 抽奖活动表Dao
 */
@Mapper
public interface IRaffleActivityDao {
    RaffleActivity queryRaffleActivityByActivityId(long l);

    Long queryStrategyIdByActivityId(Long activityId);
    Long queryActivityIdByStrategyId(Long strategyId);

    Long queryTodayUserRaffleCount(String userId, Long strategyId);
}
