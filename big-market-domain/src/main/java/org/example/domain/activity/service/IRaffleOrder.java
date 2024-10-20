package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @Author atticus
 * @Date 2024/10/16 20:44
 * @description: 抽奖活动订单
 */

public interface IRaffleOrder {

    ActivityOrderEntity createActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
