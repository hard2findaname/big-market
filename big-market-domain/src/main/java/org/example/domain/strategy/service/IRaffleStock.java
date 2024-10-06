package org.example.domain.strategy.service;

import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @Author atticus
 * @Date 2024/10/06 18:07
 * @description: 抽奖库存相关服务,获取库存，消耗队列
 */
public interface IRaffleStock {
    /**
     * @description: 获取奖品库存消耗队列
     * @return: 奖品库存key
     **/
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;
    /**
     * @description:    更新奖品库存消耗信息
     * @param: strategyId   策略ID
     * @param: awardId      奖品ID
     * @return: void
     **/
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
