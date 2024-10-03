package org.example.domain.strategy.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import org.example.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.assembly.IStrategyDispatch;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

/**
 * @Author atticus
 * @Date 2024/09/29 23:54
 * @description:
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    // 策略仓储服务 -> domain层像一个大厨，仓储层提供米面粮油
    protected IStrategyRepository strategyRepository;

    // 策略调度服务 -> 只负责抽奖处理，通过新增接口的方式，隔离职责，不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    private DefaultLogicChainFactory logicChainFactory;
    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultLogicChainFactory logicChainFactory) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
        this.logicChainFactory = logicChainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1、参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(null == strategyId || StringUtils.isBlank(userId)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 责任链
        ILogicChain logicChain = logicChainFactory.openLogicChain(strategyId);
        Integer awardId = logicChain.logic(userId, strategyId);


        //3.过滤抽奖中规则
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModelVO(strategyId,awardId);

        //4.抽奖中-规则过滤
        RuleActionEntity<RuleActionEntity.RaffleWhileEntity> raffleWhileEntityRuleActionEntity= this.doCheckRaffleDuringLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleDuringRuleModelList());
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(raffleWhileEntityRuleActionEntity.getCode())) {
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder().awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }
    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
    protected abstract RuleActionEntity<RuleActionEntity.RaffleWhileEntity> doCheckRaffleDuringLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}
