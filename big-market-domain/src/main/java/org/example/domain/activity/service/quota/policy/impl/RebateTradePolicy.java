package org.example.domain.activity.service.quota.policy.impl;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.valobj.OrderStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2025/04/14 13:20
 * @description:
 */
@Service("rebate_trade")
public class RebateTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.complete);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveOrder(createQuotaOrderAggregate);


    }
}
