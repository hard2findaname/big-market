package org.example.domain.strategy.service.rule.chain;

/**
 * @Author atticus
 * @Date 2024/10/02 23:54
 * @description: 责任链接口
 */
public interface ILogicChain extends ILogicChainAssembly{

    /**
     * @description: 责任链接口
     * @param: userId       用户ID
     * @param: strategyId   策略ID
     * @return: Integer     奖品ID
     **/
    Integer logic(String userId, Long strategyId);


}
