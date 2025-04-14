package org.example.domain.credit.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.credit.model.aggregate.TradeAggregate;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.CreditOrderEntity;
import org.example.domain.credit.model.entity.TradeEntity;
import org.example.domain.credit.repository.IUserCreditOrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 2025/04/13 21:12
 * @description:
 */
@Service
@Slf4j
public class UserCreditAdjustServiceImpl implements IUserCreditAdjustService {
    @Resource
    private IUserCreditOrderRepository userCreditOrderRepository;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        log.info("增加积分额度");
        // 1. 创建积分账户实体
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(
                tradeEntity.getUserId(),
                tradeEntity.getAmount());

        // 2. 创建积分订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(
                tradeEntity.getUserId(),
                tradeEntity.getTradeName(),
                tradeEntity.getTradeType(),
                tradeEntity.getAmount(),
                tradeEntity.getOutBusinessNo());

        // 3.构建聚合对象
        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditAccountEntity(creditAccountEntity)
                .creditOrderEntity(creditOrderEntity)
                .build();
        // 4. 保存积分交易订单
        userCreditOrderRepository.saveUserCreditOrder(tradeAggregate);
        log.info("增加账户积分额度完成 userId:{} orderId:{}", tradeEntity.getUserId(), creditOrderEntity.getOrderId());


        return creditOrderEntity.getOrderId();
    }
}
