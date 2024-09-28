package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Award;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/09/27 22:32
 * @description: 奖品表dao
 */
@Mapper
public interface IAwardDAO {

    List<Award> queryAwardList();
}
