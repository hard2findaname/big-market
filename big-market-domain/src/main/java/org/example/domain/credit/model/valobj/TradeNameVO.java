package org.example.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2025/04/13 17:59
 * @description:
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {
    REBATE("行为返利"),
    CONVERT_SKU("兑换抽奖"),

    ;
    private final String name;

}
