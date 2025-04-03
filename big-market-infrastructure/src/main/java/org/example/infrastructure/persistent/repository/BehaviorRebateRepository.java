package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;
import org.example.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.infrastructure.event.EventPublisher;
import org.example.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author atticus
 * @Date 2025/04/02 22:13
 * @description:
 */
@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {
    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;

    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private EventPublisher eventPublisher;
    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebate(BehaviorTypeVO behaviorTypeVO) {

        List<DailyBehaviorRebate> dailyBehaviorRebateList = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());

        if(null == dailyBehaviorRebateList || dailyBehaviorRebateList.isEmpty()) return new ArrayList<>();

        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOList = new ArrayList<>(dailyBehaviorRebateList.size());
        for(DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebateList){
            DailyBehaviorRebateVO dailyBehaviorRebateVO = DailyBehaviorRebateVO.builder()
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .build();
            dailyBehaviorRebateVOList.add(dailyBehaviorRebateVO);
        }
        return dailyBehaviorRebateVOList;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregateList) {
        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
               try{
                   for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregateList){
                       BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
                       // 用户行为返利订单对象
                       UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                       userBehaviorRebateOrder.setUserId(behaviorRebateOrderEntity.getUserId());
                       userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                       userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                       userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                       userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                       userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                       userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                       userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                       //任务对象
                       TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();

                       Task task = new Task();

                       task.setUserId(taskEntity.getUserId());
                       task.setTopic(taskEntity.getTopic());
                       task.setMessageId(taskEntity.getMessageId());
                       task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                       task.setState(taskEntity.getState().getCode());
                       taskDao.insert(task);

                   }
                   return 1;
               }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                   log.error("\n写入返利记录，唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
               }
            });
        }finally {
            dbRouter.clear();
        }
        // 发送MQ消息
        for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregateList){
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(taskEntity.getUserId());
            task.setMessageId(taskEntity.getMessageId());
            try {
                // 发送消息
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());

                taskDao.updateTaskMessageStatus_Completed(task);
            } catch (Exception e){
                log.error("写入返利记录，发送消息失败 userId: {}, topic: {}", userId, task.getTopic());
                taskDao.updateTaskMessageStatus_Failed(task);
            }
        }
    }
}
