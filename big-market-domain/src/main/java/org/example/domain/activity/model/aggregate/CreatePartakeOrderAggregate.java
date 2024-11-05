package org.example.domain.activity.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.entity.ActivityAccountDayEntity;
import org.example.domain.activity.model.entity.ActivityAccountEntity;
import org.example.domain.activity.model.entity.ActivityAccountMonthEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * @Author atticus
 * @Date 2024/10/30 23:14
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePartakeOrderAggregate {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    private ActivityAccountEntity activityAccountEntity;

    private boolean isExistAccountMonth = true;

    private ActivityAccountDayEntity activityAccountDayEntity;

    private boolean isExistAccountDay = true;
    private ActivityAccountMonthEntity activityAccountMonthEntity;

    private UserRaffleOrderEntity userRaffleOrderEntity;
}
