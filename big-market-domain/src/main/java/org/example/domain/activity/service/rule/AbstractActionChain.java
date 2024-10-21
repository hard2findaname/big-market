package org.example.domain.activity.service.rule;

/**
 * @Author atticus
 * @Date 2024/10/20 22:55
 * @description:
 */
public abstract class AbstractActionChain implements IActionChain{
    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
