package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Strategy;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/09/28 17:46
 * @description: 抽奖策略 DAO
 */
@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}
