package org.example.domain.activity.model.valobj;

import lombok.*;

/**
 * @Author atticus
 * @Date 2024/10/23 22:54
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySKUStockKeyVO {
    private Long sku;
    private Long activityId;
}
