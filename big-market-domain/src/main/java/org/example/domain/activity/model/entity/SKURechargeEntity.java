package org.example.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.activity.model.valobj.OrderTradeTypeVO;

/**
 * @Author atticus
 * @Date 2024/10/20 15:31
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SKURechargeEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
    /**
     * 幂等业务单号，外部谁充值谁透传，这样来保证幂等（多次调用也能确保结果唯一，不会多次充值）。
     */
    private String outBusinessNo;

    private OrderTradeTypeVO orderTradeTypeVO = OrderTradeTypeVO.rebate_trade;
}
