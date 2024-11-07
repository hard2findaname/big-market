package org.example.domain.task.Service;

import org.example.domain.task.model.entity.TaskEntity;
import org.example.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author atticus
 * @Date 2024/11/07 13:51
 * @description:
 */
@Service
public class TaskService implements ITaskService{
    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNotSendMessageTaskList() {
        return taskRepository.queryNotSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        taskRepository.updateTaskSendMessageCompleted(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFailed(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFailed(userId, messageId);
    }
}
