package org.example.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/10/16 22:31
 * @description: 订单状态
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {
    complete("complete", "完成");
    private final String code;
    private final String info;
}
