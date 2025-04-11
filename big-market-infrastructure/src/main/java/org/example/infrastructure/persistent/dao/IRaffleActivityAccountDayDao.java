package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccountDay;

/**
 * @Author atticus
 * @Date 2024/10/30 17:42
 * @description: 用户抽奖活动账户日记录表查询
 */
@Mapper
public interface IRaffleActivityAccountDayDao {
    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);

    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay raffleActivityAccountDay);

    void insertActivityAccountDay(RaffleActivityAccountDay raffleActivityAccountDay);

    @DBRouter
    Integer queryAccountDailyLottery(RaffleActivityAccountDay activityAccountDay);

    void addAccountQuota(RaffleActivityAccountDay raffleActivityAccountDay);
}
