package org.example.trigger.api;

import org.example.trigger.api.dto.ActivityDrawRequestDTO;
import org.example.trigger.api.dto.ActivityDrawResponseDTO;
import org.example.types.model.Response;

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
}
