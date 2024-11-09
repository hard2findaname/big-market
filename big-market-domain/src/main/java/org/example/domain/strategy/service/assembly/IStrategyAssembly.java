package org.example.domain.strategy.service.assembly;

/**
 * @Author atticus
 * @Date 2024/09/28 18:52
 * @description: 策略装配,根据数据库抽奖概率装配到系统中
 */
public interface IStrategyAssembly {


    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param activityId 活动Id
     * @return
     */
    boolean assembleLotteryStrategyByActivityId(Long activityId);
    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
