package org.example.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.model.valobj.ActivitySKUStockKeyVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.Assembly.IActivityDispatch;
import org.example.domain.activity.service.rule.AbstractActionChain;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2024/10/20 23:01
 * @description:
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivityStockActionChain extends AbstractActionChain{
    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        // 扣减库存
        boolean success = activityDispatch.subActivitySKUStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        // 库存扣减成功
        if(success){
            log.info("活动责任链-商品库存处理成功");

            // 写入延迟队列， 延迟更新库存记录
            activityRepository.activitySKUStockConsumeSendQueue(ActivitySKUStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());
            return true;
        }else{
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
        }

    }
}
