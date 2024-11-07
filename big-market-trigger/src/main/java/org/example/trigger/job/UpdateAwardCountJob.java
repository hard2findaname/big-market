package org.example.trigger.job;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import org.example.domain.strategy.service.IRaffleStock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2024/10/06 18:05
 * @description: 更新奖品库存任务：redis更新缓存，异步更新数据库，最终数据一致
 */
@Slf4j
@Component()
public class UpdateAwardCountJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            StrategyAwardStockKeyVO stockKeyVO = raffleStock.takeQueueValue();
            if(null == stockKeyVO)return;
            log.info("定时任务，更新奖品消耗库存 strategyId:{} awardId:{}", stockKeyVO.getStrategyId(), stockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(stockKeyVO.getStrategyId(),stockKeyVO.getAwardId());
        } catch (Exception e) {
            log.error("定时任务，更新奖品消耗库存失败", e);
        }
    }
}
