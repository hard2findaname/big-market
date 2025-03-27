package org.example.domain.strategy.service.assembly;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/09/29 17:11
 * @description:
 */
public interface IStrategyDispatch {
    /**
     * @description: 获取抽奖策略装配完成后的随机抽奖结果
     * @date:  2024-09-29 18:11
     * @param: strategyId
     * @return: Integer
     **/
    Integer getRandomAwardId(Long strategyId);
    /**
     *
     * @param: strategyId
     * @param: ruleWeightValue
     * @return: Integer
     **/
    Integer getRandomAwardId(Long strategyId,String ruleWeightValue);

    Integer getRandomAwardId(String key);
    /**
     *  根据策略ID和奖品ID，扣减奖品缓存与库存
     * @param: strategyId   策略ID
     * @param: awardId      奖品ID
     * @param: endDateTime  活动借书时间
     * @return: Boolean     扣减结果
     **/
    Boolean subAwardStock(Long strategyId, Integer awardId, Date endDateTime);
}
