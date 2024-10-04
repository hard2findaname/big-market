package org.example.domain.strategy.service.rule.tree;

import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @Author atticus
 * @Date 2024/10/03 20:18
 * @description:
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);

}
