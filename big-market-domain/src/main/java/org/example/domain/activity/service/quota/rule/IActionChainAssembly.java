package org.example.domain.activity.service.quota.rule;

/**
 * @Author atticus
 * @Date 2024/10/20 22:53
 * @description:
 */
public interface IActionChainAssembly {
    IActionChain next();
    IActionChain appendNext(IActionChain next);
}
