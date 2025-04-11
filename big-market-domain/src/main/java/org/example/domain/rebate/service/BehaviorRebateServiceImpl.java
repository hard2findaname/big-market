package org.example.domain.rebate.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import org.example.domain.rebate.model.valobj.TaskStatusVO;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.types.common.Constants;
import org.example.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 21:27
 * @description:
 */
@Service
public class BehaviorRebateServiceImpl implements IBehaviorRebateService {
    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;

    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;
    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        //1. 查询配置信息
        List<DailyBehaviorRebateVO> behaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebate(behaviorEntity.getBehaviorTypeVO());
        if(null == behaviorRebateVOS || behaviorRebateVOS.isEmpty()) return new ArrayList<>();
        //2. 构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregateList = new ArrayList<>();
        for(DailyBehaviorRebateVO dailyBehaviorRebateVO : behaviorRebateVOS){
            // 拼装业务ID； 用户ID_返利类型_外部透传业务ID
            String bizID = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizID)
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());


            // MQ 消息对象建立
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .bizId(bizID)
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .userId(behaviorEntity.getUserId())
                    .build();
            // 构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            //组装任务对象
            TaskEntity taskEntity = TaskEntity.builder()
                    .userId(behaviorEntity.getUserId())
                    .message(rebateMessageEventMessage)
                    .messageId(rebateMessageEventMessage.getId())
                    .topic(sendRebateMessageEvent.topic())
                    .state(TaskStatusVO.create)
                    .build();
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .userId(behaviorEntity.getUserId())
                    .build();

            behaviorRebateAggregateList.add(behaviorRebateAggregate);

        }
        //3. 存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregateList);
        //4. 返回订单ID集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByBusinessNo(String userId, String outBusinessNo) {

        return behaviorRebateRepository.queryOrderByBusinessNo(userId, outBusinessNo);
    }
}
