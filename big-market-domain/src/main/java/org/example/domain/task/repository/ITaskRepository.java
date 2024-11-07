package org.example.domain.task.repository;

import org.example.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/11/07 13:49
 * @description:
 */
public interface ITaskRepository {
    List<TaskEntity> queryNotSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFailed(String userId, String messageId);
}
