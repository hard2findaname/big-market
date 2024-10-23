package org.example.domain.activity.service.Assembly;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/10/23 21:48
 * @description: 活动调度【扣减库存】
 */
public interface IActivityDispatch {
    public boolean subActivitySKUStock(Long sku, Date endDateTime);
}
