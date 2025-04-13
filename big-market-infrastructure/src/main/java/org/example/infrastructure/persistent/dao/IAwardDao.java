package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Award;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/09/28 17:43
 * @description:
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();

    String queryAwardConfig(Integer awardId);

    String queryAwardKey(Integer awardId);
}
