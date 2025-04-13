package org.example.domain.award.repository;

import org.example.domain.award.model.aggregate.DispatchAwardsAggregate;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * @Author atticus
 * @Date 2024/11/06 20:22
 * @description:
 */
public interface IAwardRepository {
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    String queryAwardConfig(Integer awardId);

    void saveDispatchAwardsAggregate(DispatchAwardsAggregate dispatchAwardsAggregate);

    String queryAwardKey(Integer awardId);
}
