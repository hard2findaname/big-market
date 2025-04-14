package org.example.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.credit.model.valobj.TradeNameVO;
import org.example.domain.credit.model.valobj.TradeTypeVO;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2025/04/13 17:59
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeEntity {
    /** 用户ID */
    private String userId;
    /** 交易名称 */
    private TradeNameVO tradeName;
    /** 交易类型；交易类型；forward-正向、reverse-逆向 */
    private TradeTypeVO tradeType;
    /** 交易金额 */
    private BigDecimal amount;
    /** 业务仿重ID - 外部透传。返利、行为等唯一标识 */
    private String outBusinessNo;

}
