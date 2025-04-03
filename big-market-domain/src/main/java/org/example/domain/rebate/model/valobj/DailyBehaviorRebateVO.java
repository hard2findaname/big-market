package org.example.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2025/04/02 21:30
 * @description: 每日登录行为返利
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBehaviorRebateVO {
    /** 行为类型（sign 签到、openai_pay 支付） */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
}
