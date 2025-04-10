package org.example.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2025/04/03 15:40
 * @description: 返利类型 (sku 活动库存充值商品， integral 用户活动积分)
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {
    SKU("sku", "活动库存充值商品"),
    INTEGRAL("integral", "用户活动积分"),
    ;

    private final String code;
    private final String info;

}
