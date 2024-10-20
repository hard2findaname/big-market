package org.example.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/10/16 22:31
 * @description: 活动状态
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {
    create("create","创建");
    private final String code;
    private final String info;
}
