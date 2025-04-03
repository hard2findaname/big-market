package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 17:33
 * @description: 用户日常行为返利活动配置
 */
@Mapper
public interface IDailyBehaviorRebateDao {
    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String behaviorState);
}
