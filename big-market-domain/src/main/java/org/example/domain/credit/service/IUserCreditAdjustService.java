package org.example.domain.credit.service;

import org.example.domain.credit.model.entity.TradeEntity;

/**
 * @Author atticus
 * @Date 2025/04/13 17:52
 * @description:
 */
public interface IUserCreditAdjustService {

    String createOrder(TradeEntity tradeEntity);
}
