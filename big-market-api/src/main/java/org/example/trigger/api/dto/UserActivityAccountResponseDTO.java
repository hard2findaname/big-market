package org.example.trigger.api.dto;

import lombok.Data;

/**
 * @Author atticus
 * @Date 2025/04/11 10:30
 * @description:
 */
@Data
public class UserActivityAccountResponseDTO {

    private Integer totalCount;

    private Integer totalCountSurplus;

    private Integer dayCount;

    private Integer dayCountSurplus;

    private Integer monthCount;

    private Integer monthCountSurplus;
}
