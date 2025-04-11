package org.example.domain.strategy.repository;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.valobj.RuleTreeVO;
import org.example.domain.strategy.model.valobj.RuleWeightVO;
import org.example.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/09/28 18:55
 * @description: 策略仓储接口
 */
public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    Integer getStrategyAwardAssemble(String key, Integer rateKey);

    int getRateRange(Long strategyId);
    int getRateRange(String  key);
    /**
     * @description: 根据策略ID（StrategyId）查询策略表实体
     * @param: strategyId
     * @return: StrategyEntity
     **/
    StrategyEntity queryStrategyByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);
    String queryStrategyRuleValue(Long strategyId,  String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    /**
     * @description: 根据规则树ID，查询树结构信息
     * @param: treeId 规则树ID
     * @return: RuleTreeVO 树结构信息
     **/
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);
    /**
     * @description: 缓存奖品库存
     * @param: cacheKey     自定义的reids key
     * @param: awardCount   库存值
     * @return: void
     **/
    void cacheStrategyAwardCount(String cacheKey, Integer awardCount);
    /**
     * 缓存Key，decr 方式扣减库存
     *
     * @param: cacheKey     缓存Key
     * @return: Boolean     扣减结果
     **/
    Boolean subAwardStock(String cacheKey);
    /**
     * 缓存Key，decr 方式扣减库存
     *
     * @param: cacheKey     缓存Key
     * @param: endDateTime  活动结束时间
     * @return: Boolean     扣减结果
     **/
    Boolean subAwardStock(String cacheKey, Date endDateTime);
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);
    /**
     *
     * @return: StrategyAwardStockKeyVO
     **/
    StrategyAwardStockKeyVO takeQueueValue();
    /**
     * @description:
     * 更新奖品库存
     * @param: strategyId
     * @param: awardId
     * @return: void
     **/
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
    /**
     * @description:
     * 根据 策略ID+ 奖品ID 的唯一组合 查询奖品信息
     * @param: strategyId
     * @param: awardId
     * @return: StrategyAwardEntity
     **/
    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserRaffleCount(String userId, Long strategyId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    Integer queryActivityAccountTotalUse(String userId, Long strategyId);

    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);
}
