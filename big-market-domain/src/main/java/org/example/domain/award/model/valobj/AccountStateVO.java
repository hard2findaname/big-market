package org.example.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2025/04/11 21:44
 * @description:
 */
@Getter
@AllArgsConstructor
public enum AccountStateVO {

    open("open", "开启"),
    close("close","关闭"),
    ;
    private final String code;
    private final String info;
}
