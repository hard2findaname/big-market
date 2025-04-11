package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 17:34
 * @description: 用户行为返利流水
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);
    @DBRouter
    List<UserBehaviorRebateOrder> queryOrderByBusinessNo(UserBehaviorRebateOrder userBehaviorRebateOrderReq);
}
