package org.example.domain.strategy.service.rule.chain;

/**
 * @Author atticus
 * @Date 2024/10/03 00:00
 * @description:
 */
public abstract class AbstractLogicChain implements ILogicChain{
    private ILogicChain next;


    @Override
    public ILogicChain appendNext(ILogicChain logicChain) {
        this.next = logicChain;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();
}
