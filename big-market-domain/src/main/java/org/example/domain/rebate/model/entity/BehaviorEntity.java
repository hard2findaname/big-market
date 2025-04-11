package org.example.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;

/**
 * @Author atticus
 * @Date 2025/04/02 21:22
 * @description: 用户行为动作实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorEntity {

    private String userId;

    private BehaviorTypeVO behaviorTypeVO;
    /** 业务防重ID */
    private String outBusinessNo;
}
