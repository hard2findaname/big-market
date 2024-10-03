package org.example.domain.strategy.service.rule.chain.factory;

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

        ILogicChain logicChain = logicChainMap.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for(int i =1; i < ruleModels.length; i++){
            ILogicChain logicChain1 = logicChainMap.get(ruleModels[i]);
            current = current.appendNext(logicChain1);
        }

        current.appendNext(logicChainMap.get("default"));

        return logicChain;
    }
}
