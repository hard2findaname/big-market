package org.example.domain.rebate.service;

import org.example.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 21:21
 * @description: 行为返利接口
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity behaviorEntity);
}
