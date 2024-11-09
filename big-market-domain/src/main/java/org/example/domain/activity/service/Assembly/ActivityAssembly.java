package org.example.domain.activity.service.Assembly;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author atticus
 * @Date 2024/10/23 21:40
 * @description: 活动SKU
 */
@Slf4j
@Service
public class ActivityAssembly implements IActivityAssembly, IActivityDispatch{
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public boolean assembleActivitySKU(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        cacheActivitySKUStockCount(sku, activitySkuEntity.getStockCount());
        // 查询活动【存到缓存】
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 查询活动次数【】
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    @Override
    public boolean assembleActivitySKUByActivityId(Long activityId) {
        List<ActivitySkuEntity> activitySkuEntityList = activityRepository.queryActivitySkuListByActivityId(activityId);
        for(ActivitySkuEntity activitySkuEntity : activitySkuEntityList){
            cacheActivitySKUStockCount(activitySkuEntity.getSku(), activitySkuEntity.getStockCount());
            // 查询活动【存到缓存】
            activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        }
        activityRepository.queryRaffleActivityByActivityId(activityId);
        return true;
    }

    private void cacheActivitySKUStockCount(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySKUStockCount(cacheKey, stockCount);
        log.info("装配完成: redis存储 sku：{},count：{}",cacheKey,stockCount);
    }

    @Override
    public boolean subActivitySKUStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subActivitySKUStock(sku, cacheKey, endDateTime);
    }
}
