package org.example.domain.award.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.domain.award.model.valobj.AwardStateVO;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2025/04/11 21:26
 * @description: 发放奖品聚合
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispatchAwardsAggregate {
    /** 用户ID */
    private String userId;
    /** 用户积分奖品 */
    private UserCreditAwardEntity userCreditAwardEntity;
    /** 用户发奖记录 */
    private UserAwardRecordEntity userAwardRecordEntity;

    public static UserAwardRecordEntity buildDispatchUserAwardRecordEntity(String userId, String orderId, Integer awardId, AwardStateVO awardState) {
        UserAwardRecordEntity userAwardRecord = new UserAwardRecordEntity();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setOrderId(orderId);
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setAwardState(awardState);
        return userAwardRecord;
    }

    public static UserCreditAwardEntity buildUserCreditAwardEntity(String userId, BigDecimal creditAmount) {
        return UserCreditAwardEntity.builder().userId(userId).creditAmount(creditAmount).build();
    }

}
