package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson2.JSON;
import jdk.nashorn.internal.runtime.ECMAException;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.award.model.aggregate.DispatchAwardsAggregate;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.entity.TaskEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.domain.award.model.valobj.AccountStateVO;
import org.example.domain.award.repository.IAwardRepository;
import org.example.infrastructure.event.EventPublisher;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserAwardRecord;
import org.example.infrastructure.persistent.po.UserCreditAccount;
import org.example.infrastructure.persistent.po.UserRaffleOrder;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.redisson.api.RLock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author atticus
 * @Date 2024/11/06 20:30
 * @description:
 */
@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IAwardDao awardDao;

    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private IRedisService redisService;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();

        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        UserRaffleOrder userRaffleOrderReq = new UserRaffleOrder();
        userRaffleOrderReq.setUserId(userAwardRecordEntity.getUserId());
        userRaffleOrderReq.setOrderId(userAwardRecordEntity.getOrderId());
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    // 写入任务
                    taskDao.insert(task);
                    //更新订单
                    int count = userRaffleOrderDao.updateUserRaffleOrderUsed(userRaffleOrderReq);
                    if(1 != count){
                        status.setRollbackOnly();
                        log.error("写入中奖记录，用户抽奖单已使用，不可重复抽奖 userId: {} activityId: {} awardId: {}", userId, activityId, awardId);
                        throw new AppException(ResponseCode.ACTIVITY_ORDER_ERROR.getCode(),ResponseCode.ACTIVITY_ORDER_ERROR.getInfo());
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);

                }
            });
        }finally {
            dbRouter.clear();
        }
        try {
                log.info("写入中奖记录，发送MQ消息 userId: {} topic: {}", userId, task.getTopic());
                // 发送消息【在事务外执行，如果失败还有任务补偿】
                eventPublisher.publish(task.getTopic(),task.getMessage());
                // 更新数据库记录，task 任务表
                taskDao.updateTaskMessageStatus_Completed(task);
            }catch (Exception e){
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskMessageStatus_Failed(task);
        }
    }

    @Override
    public String queryAwardConfig(Integer awardId) {
        return awardDao.queryAwardConfig(awardId);
    }

    @Override
    public void saveDispatchAwardsAggregate(DispatchAwardsAggregate dispatchAwardsAggregate) {
        String userId = dispatchAwardsAggregate.getUserId();
        UserCreditAwardEntity userCreditAwardEntity = dispatchAwardsAggregate.getUserCreditAwardEntity();
        UserAwardRecordEntity userAwardRecordEntity = dispatchAwardsAggregate.getUserAwardRecordEntity();

        // 更新发奖记录
        UserAwardRecord userAwardRecordReq = new UserAwardRecord();
        userAwardRecordReq.setUserId(userId);
        userAwardRecordReq.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecordReq.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        // 更新用户积分 「首次则插入数据」
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userCreditAwardEntity.getUserId());
        userCreditAccountReq.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccountReq.setAccountState(AccountStateVO.open.getCode());
        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try{
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 更新积分
                    UserCreditAccount userCreditAccountRes = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if(null == userCreditAccountRes){
                        userCreditAccountDao.insert(userCreditAccountReq);
                    }else{
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }

                    // 更新奖品记录
                    int updateAwardCount = userAwardRecordDao.updateAwardRecordCompletedState(userAwardRecordReq);
                    if(0 == updateAwardCount){
                        log.warn("更新中奖记录，重复更新拦截");
                        status.setRollbackOnly();
                    }

                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("更新中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}",
                            userAwardRecordEntity.getUserId(),
                            userAwardRecordEntity.getActivityId(),
                            userAwardRecordEntity.getAwardId());
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }catch (Exception e){
                    log.error("发生错误，定位:\n");
                    throw new RuntimeException(e);
                }
            });
        }finally {
            dbRouter.clear();
            lock.unlock();
        }
    }

    @Override
    public String queryAwardKey(Integer awardId) {
        //todo 此处查询到奖品 key 之后 可以选择存入缓存
        return awardDao.queryAwardKey(awardId);
    }
}
