package org.example.domain.activity.service;

import org.example.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @Author atticus
 * @Date 2024/10/20 11:54
 * @description:
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity{
    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

}
