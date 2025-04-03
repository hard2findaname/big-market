package org.example.domain.rebate.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.TaskEntity;

/**
 * @Author atticus
 * @Date 2025/04/02 21:42
 * @description: 行为返利聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    String userId;

    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;

    private TaskEntity taskEntity;

}
