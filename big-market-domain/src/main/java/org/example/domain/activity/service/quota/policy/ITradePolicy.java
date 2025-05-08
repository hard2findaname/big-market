package org.example.domain.activity.service.quota.policy;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * @Author atticus
 * @Date 2025/04/14 13:17
 * @description:
 */
public interface ITradePolicy {

    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
