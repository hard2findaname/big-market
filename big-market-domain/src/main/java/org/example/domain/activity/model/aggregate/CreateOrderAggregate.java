package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityAccountEntity;
import org.example.domain.activity.model.entity.ActivityOrderEntity;

/**
 * @Author atticus
 * @Date 2024/10/16 22:42
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    /**
     * 活动账号对象
     */
    private ActivityAccountEntity activityAccountEntity;
    /**
     * 活动订单对象
     */
    private ActivityOrderEntity activityOrderEntity;
}
