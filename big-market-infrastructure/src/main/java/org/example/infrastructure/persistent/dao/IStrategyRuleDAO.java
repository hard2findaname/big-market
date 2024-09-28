package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.StrategyRule;

import java.util.List;
/**
 * @Author atticus
 * @Date 2024/09/27 22:33
 * @description:
 */
@Mapper
public interface IStrategyRuleDAO {
    List<StrategyRule> queryStrategyRuleList();
}
