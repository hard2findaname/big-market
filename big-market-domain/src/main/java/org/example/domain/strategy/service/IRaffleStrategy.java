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

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);


}
