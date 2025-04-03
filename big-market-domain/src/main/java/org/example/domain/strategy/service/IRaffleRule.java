package org.example.domain.strategy.service;

import java.util.Map;

/**
 * @Author atticus
 * @Date 2025/03/24 13:05
 * @description: 抽奖规则接口
 */
public interface IRaffleRule {

    Map<String, Integer> queryAwardRuleLockCount(String... treeIds);
}
