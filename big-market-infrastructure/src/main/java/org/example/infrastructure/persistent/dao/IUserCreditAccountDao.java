package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserCreditAccount;

/**
 * @Author atticus
 * @Date 2025/04/11 21:05
 * @description:
 */
@Mapper
public interface IUserCreditAccountDao {    

    int updateAddAmount(UserCreditAccount userCreditAccountReq);

    void insert(UserCreditAccount userCreditAccountReq);

    UserCreditAccount queryUserCreditAccount(UserCreditAccount userCreditAccountReq);
}
