package org.example.domain.rebate.repository;

import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;
import org.example.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 21:28
 * @description: 行为返利服务仓储接口
 */
public interface IBehaviorRebateRepository {
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebate(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregateList);
}
