package org.example.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/10/30 23:01
 * @description:
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {
    create("create","创建"),
    used("used","已使用"),
    cancel("cancel","已取消"),

    ;

    private final String code;
    private final String info;
}
