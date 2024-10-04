package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @Author atticus
 * @Date 2024/10/03 18:33
 * @description:
 */
@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {
    EQUAL(1, "等于"),
    GT(2, "大于"),
    LT(3, "小于"),
    GE(4, "大于&等于"),
    LE(5, "小于&等于"),
    ENUM(6, "枚举"),
    ;

    private final Integer code;
    private final String info;

}
