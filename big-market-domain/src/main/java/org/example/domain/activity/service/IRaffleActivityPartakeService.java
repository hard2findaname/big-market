package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.PartakeRaffleActivityEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.model.valobj.UserRaffleOrderStateVO;

/**
 * @Author atticus
 * @Date 2024/10/30 22:54
 * @description:
 */
public interface IRaffleActivityPartakeService {

    UserRaffleOrderEntity createOrder(String userId, Long activityId);


    /**
     *
     * @param: partakeRaffleActivityEntity
     * @return: UserRaffleOrderStateVO
     **/
    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
