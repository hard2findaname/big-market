package org.example.domain.activity.service.Assembly;

/**
 * @Author atticus
 * @Date 2024/10/23 21:39
 * @description: 活动装配接口
 */
public interface IActivityAssembly {
    boolean assembleActivitySKU(Long sku);

    boolean assembleActivitySKUByActivityId(Long activityId);
}
