package org.example.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/10/03 11:23
 * @description: 责任链装配工厂
 */
@Service
public class DefaultLogicChainFactory {

    private final Map<String, ILogicChain> logicChainMap;

    private final IStrategyRepository strategyRepository;

    public DefaultLogicChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository strategyRepository) {
        this.logicChainMap = logicChainMap;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain openLogicChain(Long strategyId){
        // 通过策略Id 获取策略实体（策略具体信息）
        StrategyEntity strategyEntity = strategyRepository.queryStrategyByStrategyId(strategyId);
        // 主要是要获得 ruleModels
        String[] ruleModels = strategyEntity.ruleModels();
        if(null == ruleModels || 0 == ruleModels.length)  return logicChainMap.get("default");


        // 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight 「注意此数据从Redis缓存中获取，如果更新库表，记得在测试阶段手动处理缓存」

        ILogicChain logicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for(int i =1; i < ruleModels.length; i++){
            ILogicChain logicChain1 = logicChainMap.get(ruleModels[i]);
            current = current.appendNext(logicChain1);
        }

        current.appendNext(logicChainMap.get("default"));

        return logicChain;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor

    public static class StrategyAwardVO{
        /** 抽奖奖品ID - 内部流转使用 */
        private Integer awardId;
        /**  */
        private String logicModel;

    }
    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }

}
