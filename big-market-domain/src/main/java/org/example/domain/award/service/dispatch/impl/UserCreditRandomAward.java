package org.example.domain.award.service.dispatch.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.domain.award.model.aggregate.DispatchAwardsAggregate;
import org.example.domain.award.model.entity.DispatchAwardEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.domain.award.model.valobj.AwardStateVO;
import org.example.domain.award.repository.IAwardRepository;
import org.example.domain.award.service.dispatch.IDispatchAward;
import org.example.types.common.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @Author atticus
 * @Date 2025/04/11 21:13
 * @description:
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDispatchAward {
    @Resource
    private IAwardRepository awardRepository;

    @Override
    public void distributeAwards(DispatchAwardEntity dispatchAwardEntity) {

        Integer awardId = dispatchAwardEntity.getAwardId();

        String awardConfig = dispatchAwardEntity.getAwardConfig();

        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = awardRepository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2) {
            throw new RuntimeException("积分值设置不匹配");
        }
        // 生成随机积分
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));

        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = DispatchAwardsAggregate.buildDispatchUserAwardRecordEntity(
                dispatchAwardEntity.getUserId(),
                dispatchAwardEntity.getOrderId(),
                dispatchAwardEntity.getAwardId(),
                AwardStateVO.complete
        );

        UserCreditAwardEntity userCreditAwardEntity = DispatchAwardsAggregate.buildUserCreditAwardEntity(
                dispatchAwardEntity.getUserId(),
                creditAmount
        );
        DispatchAwardsAggregate dispatchAwardsAggregate = new DispatchAwardsAggregate();
        dispatchAwardsAggregate.setUserId(dispatchAwardEntity.getUserId());
        dispatchAwardsAggregate.setUserCreditAwardEntity(userCreditAwardEntity);
        dispatchAwardsAggregate.setUserAwardRecordEntity(userAwardRecordEntity);

        // 存储聚合对象
        awardRepository.saveDispatchAwardsAggregate(dispatchAwardsAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        if (min.equals(max)) return min;
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
