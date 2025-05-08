package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityOrderEntity;
import org.example.domain.activity.model.valobj.OrderStateVO;

/**
 * @Author atticus
 * @Date 2024/10/16 22:42
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuotaOrderAggregate {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 增加；总次数
     */
    private Integer totalCount;

    /**
     * 增加；日次数
     */
    private Integer dayCount;

    /**
     * 增加；月次数
     */
    private Integer monthCount;

    /**
     * 活动订单对象
     */
    private ActivityOrderEntity activityOrderEntity;

    public void setOrderState(OrderStateVO orderState){
        this.activityOrderEntity.setState(orderState);
    }
}
