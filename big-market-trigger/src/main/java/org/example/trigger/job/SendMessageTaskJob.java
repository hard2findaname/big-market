package org.example.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.task.Service.ITaskService;
import org.example.domain.task.model.entity.TaskEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author atticus
 * @Date 2024/11/07 13:43
 * @description: 发奖MQ消息扫描发送
 */
@Slf4j
@Component()
public class SendMessageTaskJob {
    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Resource
    private IDBRouterStrategy dbRouterStrategy;


    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            //获取分库数量
            int dbCount = dbRouterStrategy.dbCount();
            for(int dbIdx = 1; dbIdx < dbCount; dbIdx++){
                int finalDbIdx = dbIdx;
                threadPoolExecutor.execute(() -> {
                    try {
                        dbRouterStrategy.setDBKey(finalDbIdx);
                        dbRouterStrategy.setTBKey(0);
                        List<TaskEntity> taskEntities = taskService.queryNotSendMessageTaskList();
                        if(taskEntities.isEmpty())return;

                        for (TaskEntity taskEntity : taskEntities){
                            //发送MQ消息
                            threadPoolExecutor.execute(() -> {
                                try {
                                    taskService.sendMessage(taskEntity);
                                    taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(), taskEntity.getMessageId());
                                }catch (Exception e){
                                    log.error("定时任务，扫描发送MQ消息失败 userId:{},topic:{}",taskEntity.getUserId(), taskEntity.getTopic());
                                    taskService.updateTaskSendMessageFailed(taskEntity.getUserId(), taskEntity.getMessageId());
                                }
                            });
                        }
                    }finally {
                        dbRouterStrategy.clear();
                    }
                });
            }
        }catch (Exception e){
            log.error("定时任务，扫描发送MQ消息失败。", e);
        }
    }
}
