package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/10/30 17:42
 * @description: 任务表，用于实现发送MQ失败的补偿
 */
@Mapper
public interface ITaskDao {
    void insert(Task task);
    @DBRouter
    void updateTaskMessageStatus_Completed(Task task);
    @DBRouter
    void updateTaskMessageStatus_Failed(Task task);
    List<Task> queryNoSendMessageTaskList();
}
