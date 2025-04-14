package org.example.domain.credit.repository;

import org.example.domain.credit.model.aggregate.TradeAggregate;

/**
 * @Author atticus
 * @Date 2025/04/13 17:49
 * @description:
 */
public interface IUserCreditOrderRepository {
    void saveUserCreditOrder(TradeAggregate tradeAggregate);
}
