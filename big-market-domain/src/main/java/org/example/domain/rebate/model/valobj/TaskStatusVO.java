package org.example.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2025/04/02 21:46
 * @description:
 */
@Getter
@AllArgsConstructor
public enum TaskStatusVO {
    create("create","创建"),
    complete("complete","发送完成"),
    fail("fail","发送失败"),
    ;
    private String code;
    private String info;
}
