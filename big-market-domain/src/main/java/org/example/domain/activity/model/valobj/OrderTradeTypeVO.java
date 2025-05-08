package org.example.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2025/04/14 13:14
 * @description:
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {
    credit_pay_trade("credit_pay_trade", "积分兑换，支付类交易"),
    rebate_trade("rebate_trade","返利奖品，非支付类交易"),
    ;
    private final String code;
    private final String desc;
}
