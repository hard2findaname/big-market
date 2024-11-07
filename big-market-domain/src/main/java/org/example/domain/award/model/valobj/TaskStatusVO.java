package org.example.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/11/06 20:19
 * @description:
 */
@Getter
@AllArgsConstructor
public enum TaskStatusVO {
    create("create","创建"),
    complete("complete","发奖完成"),
    fail("fail","发奖失败"),
    ;
    private String code;
    private String info;
}
