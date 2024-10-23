package org.example.domain.activity.repository;

import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.valobj.ActivitySKUStockKeyVO;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/10/16 22:36
 * @description: 活动仓储接口
 */
public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivitySKUStockCount(String cacheKey, Integer stockCount);

    boolean subActivitySKUStock(Long sku,String cacheKey, Date endDateTime);

    void activitySKUStockConsumeSendQueue(ActivitySKUStockKeyVO activitySKUStockKeyVO);

    ActivitySKUStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
