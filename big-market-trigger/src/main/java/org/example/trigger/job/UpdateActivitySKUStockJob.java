package org.example.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.valobj.ActivitySKUStockKeyVO;
import org.example.domain.activity.service.IRaffleActivitySkuStockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2024/10/23 23:33
 * @description: 更新活动sku库存任务
 */
@Slf4j
@Component()
public class UpdateActivitySKUStockJob {

    @Resource
    private IRaffleActivitySkuStockService stock;
    @Scheduled(cron = "0/10 * * * * ?")
    public void exec(){
        try {
            log.info("定时任务，更新活动sku库存【延迟队列获取，降低对数据库的更新频次，不要产生竞争】");
            ActivitySKUStockKeyVO activitySKUStockKeyVO = stock.takeQueueValue();
            if(null == activitySKUStockKeyVO)return;
            log.info("定时任务，更新活动sku库存 sku:{} activityId:{}", activitySKUStockKeyVO.getSku(), activitySKUStockKeyVO.getActivityId());
            stock.updateActivitySkuStock(activitySKUStockKeyVO.getSku());
        }catch (Exception e){
            log.error("定时，更新活动sku库存失败", e);
        }
    }
}
