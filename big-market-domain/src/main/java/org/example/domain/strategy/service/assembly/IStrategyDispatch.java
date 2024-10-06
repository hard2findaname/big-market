package org.example.domain.strategy.service.assembly;

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

    Integer getRandomAwardId(Long strategyId,String ruleWeightValue);

    Boolean subAwardStock(Long strategyId, Integer awardId);
}
