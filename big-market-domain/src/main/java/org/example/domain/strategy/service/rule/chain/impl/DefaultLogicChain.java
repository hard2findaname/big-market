package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.assembly.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2024/10/03 00:03
 * @description: 兜底（默认）责任链
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyDispatch strategyDispatch;
    @Override
    public Integer logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);

        return awardId;
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
