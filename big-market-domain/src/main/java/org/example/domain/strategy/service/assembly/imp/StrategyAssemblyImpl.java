package org.example.domain.strategy.service.assembly.imp;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.assembly.IStrategyAssembly;
import org.example.domain.strategy.service.assembly.IStrategyDispatch;
import org.example.types.common.Constants;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author atticus
 * @Date 2024/09/28 18:54
 * @description:
 */
@Slf4j
@Service
public class StrategyAssemblyImpl implements IStrategyAssembly, IStrategyDispatch {
    @Resource
    private IStrategyRepository strategyRepository;

    private final SecureRandom secureRandom = new SecureRandom();
    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId =  strategyRepository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1、查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);

        //2、缓存奖品设【用于扣减库存】
        for(StrategyAwardEntity strategyAward: strategyAwardEntities){
            Integer awardId = strategyAward.getAwardId();
            Integer awardCount = strategyAward.getAwardCountSurplus();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }
        // 3.1
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);


        // 3.2 权重配置， rule_weight权重规则配置
        StrategyEntity strategyEntity = strategyRepository.queryStrategyByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if(null == ruleWeight) return true;

        StrategyRuleEntity strategyRuleEntity = strategyRepository.queryStrategyRule(strategyId, ruleWeight);
        //
        if(null == strategyRuleEntity){
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo() );
        }

        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for(String key : keys){
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesCopy = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesCopy.removeIf(entity->!ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key), strategyAwardEntitiesCopy);
        }

        return true;

    }

    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        strategyRepository.cacheStrategyAwardCount(cacheKey,awardCount);

    }

    /**
     * 计算公式；
     * 1. 找到范围内最小的概率值，比如 0.1、0.02、0.003，需要找到的值是 0.003
     * 2. 基于1找到的最小值，0.003 就可以计算出百分比、千分比的整数值。这里就是1000
     * 3. 那么「概率 * 1000」分别占比100个、20个、3个，总计是123个
     * 4. 后续的抽奖就用123作为随机数的范围值，生成的值100个都是0.1概率的奖品、20个是概率0.02的奖品、最后是3个是0.003的奖品。
     */

    public void assembleLotteryStrategy(String key,List<StrategyAwardEntity> strategyAwardEntities){
        //1、获取最小概率
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 2. 循环计算找到概率范围值
        BigDecimal rateRange = BigDecimal.valueOf(convert(minAwardRate.doubleValue()));

        // 3. 生成策略奖品概率查找表「在list集合中，存放对应奖品占位，占位越多等于概率越高」
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 4. 对存储的奖品进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        // 5. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 6. 存放到 Redis
        strategyRepository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);

    }

    /**
     * 转换计算，只根据小数位来计算。如【0.01返回100】、【0.009返回1000】、【0.0018返回10000】
     */
    private double convert(double min){
        double current = min;
        double max = 1;
        while (current < 1){
            current = current * 10;
            max = max * 10;
        }
        return max;
    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        String value = String.valueOf(strategyId);
        return getRandomAwardIdFromRedis(value);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        return getRandomAwardIdFromRedis(key);

    }
    @Override
    public Integer getRandomAwardId(String key) {
        return getRandomAwardIdFromRedis(key);

    }

    @Override
    public Boolean subAwardStock(Long strategyId, Integer awardId, Date endDateTime) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;

        return strategyRepository.subAwardStock(cacheKey);
    }

    public Integer getRandomAwardIdFromRedis(String key){
        // 分布式部署下，不一定为当前应用做的策略装配。也就是值不一定会保存到本应用，而是分布式应用，所以需要从 Redis 中获取。
        int rateRange = strategyRepository.getRateRange(key);
        // 通过生成的随机值，获取概率值奖品查找表的结果
        return strategyRepository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
