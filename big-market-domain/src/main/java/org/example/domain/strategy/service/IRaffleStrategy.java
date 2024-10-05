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
 * @Date 2024/09/29 23:45
 * @description:
 */
public interface IRaffleStrategy {

    /**
     * @description: 执行抽奖；用抽奖因子入参，执行抽奖计算，返回奖品信息
     * @param: raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
     * @return: RaffleAwardEntity 抽奖的奖品
     **/
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);


}
