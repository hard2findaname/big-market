package org.example.domain.strategy.service.rule.chain;

/**
 * @Author atticus
 * @Date 2024/10/03 11:39
 * @description:
 */
public interface ILogicChainAssembly {
    ILogicChain appendNext(ILogicChain logicChain);

    ILogicChain next();
}
