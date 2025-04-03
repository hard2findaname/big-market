package org.example.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.valobj.UserRaffleOrderStateVO;

import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/10/30 23:00
 * @description: 用户抽奖订单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRaffleOrderEntity {
    /** 活动ID */
    private String userId;
    /** 活动名称 */
    private Long activityId;
    /** 抽奖策略ID */
    private String activityName;
    /** 订单ID */
    private Long strategyId;
    /** 下单时间 */
    private String orderId;
    /** 订单状态；create-创建、used-已使用、cancel-已作废 */
    private Date orderTime;
    /** 创建时间 */
    private UserRaffleOrderStateVO orderState;
    /** 结束时间*/
    private Date endDateTime;
}
