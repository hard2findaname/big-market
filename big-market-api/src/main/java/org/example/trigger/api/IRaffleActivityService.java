package org.example.trigger.api;

import org.example.trigger.api.dto.ActivityDrawRequestDTO;
import org.example.trigger.api.dto.ActivityDrawResponseDTO;
import org.example.trigger.api.dto.UserActivityAccountRequestDTO;
import org.example.trigger.api.dto.UserActivityAccountResponseDTO;
import org.example.types.model.Response;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2024/11/08 13:51
 * @description:
 */
public interface IRaffleActivityService {
    /**
     * 活动的装配，数据预热
     * @param: strategyId
     * @return: 装配结果
     **/
    Response<Boolean> activityAssembly(Long activityId);
    /**
     * 活动抽奖接口
     * @param: request 请求对象
     * @return: 返回中奖结果
     **/
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);
    /**
     * 每日签到返利接口
     * @param: userId               用户ID
     * @return: Response<Boolean>   签到获得
     **/
    Response<Boolean> dailySignRebate(String userId);
    /**
     * 查询用户每日签到是否完成
     * 根据用户Id 和 当日时间查询用户账户下当日是否行为返利奖品记录（为List<BehaviorRebateOrderEntity>）
     * list 不为空 则用户每日签到已完成
     *
     * @param: userId               用户ID
     * @return: Response<Boolean>
     **/
    Response<Boolean> isDailySignRebated(String userId);
    /**
     * 查询用户活动账户下的抽奖次数（总， 日， 月）
     *
     * @param: userActivityAccountRequestDTO                用户活动账户请求体（userId, activityId）
     * @return: Response<UserActivityAccountResponseDTO>    用户活动账户返回
     **/
    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO userActivityAccountRequestDTO);
}
