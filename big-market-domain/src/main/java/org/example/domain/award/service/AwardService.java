package org.example.domain.award.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.entity.DispatchAwardEntity;
import org.example.domain.award.model.entity.TaskEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.valobj.TaskStatusVO;
import org.example.domain.award.repository.IAwardRepository;
import org.example.domain.award.service.dispatch.IDispatchAward;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/11/06 20:23
 * @description:
 */
@Service
@Slf4j
public class AwardService extends AbstractAwardService {
    private final IAwardRepository awardRepository;
    private final SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String, IDispatchAward> dispatchAwardMap;
    public AwardService(IAwardRepository awardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, IDispatchAward> dispatchAwardMap) {
        this.awardRepository = awardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.dispatchAwardMap = dispatchAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建消息对象
        SendAwardMessageEvent.AwardMessage sendAwardMessage = new SendAwardMessageEvent.AwardMessage();
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setOrderId(userAwardRecordEntity.getOrderId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setAwardConfig(userAwardRecordEntity.getAwardConfig());

        BaseEvent.EventMessage<SendAwardMessageEvent.AwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStatusVO.create);

        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .userAwardRecordEntity(userAwardRecordEntity)
                .taskEntity(taskEntity)
                .build();
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void dispatchAward(DispatchAwardEntity dispatchAwardEntity) {
        // 奖品key
        String awardKey =  awardRepository.queryAwardKey(dispatchAwardEntity.getAwardId());
        if(null == awardKey){
            log.error("分发奖品，奖品ID不存在。awardId: {}", dispatchAwardEntity.getAwardId());
            return;
        }

        IDispatchAward dispatchAward = dispatchAwardMap.get(awardKey);
        if(null == dispatchAward){
            log.error("分发奖品对应服务不存在。 awardKey:{}",awardKey);
            //todo 后续待完善
//             throw new RuntimeException("分发奖品，奖品" + awardKey + "对应服务不存在");
            return;
        }

        //发放奖品
        dispatchAward.distributeAwards(dispatchAwardEntity);

    }
}
