package org.example.domain.award.service;

import org.example.domain.award.model.entity.UserAwardRecordEntity;

/**
 * @Author atticus
 * @Date 2024/11/06 20:06
 * @description: 奖品服务接口
 */
public interface IAwardService {

    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
}
