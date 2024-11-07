package org.example.domain.task.Service;

import org.example.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/11/07 13:46
 * @description: 消息任务服务
 */
public interface ITaskService {
    List<TaskEntity> queryNotSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFailed(String userId, String messageId);
}
