package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.dao.TaskDao;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 基础 Job 类
 *
 * Created by jonas on 15/6/6.
 */
public abstract class BaseTaskJob extends VOAbsIssueLoggable {

    private boolean running = false;

    protected String getTaskName() {
        return getTaskService().getTaskName();
    }

    protected SubSystem getSubSystem() {
        return getTaskService().getSubSystem();
    }

    protected abstract BaseTaskService getTaskService();

    public void run() {

        String taskCheck = getTaskName();

        if (running) {
            $info(taskCheck + "正在运行，忽略");
            return;
        }

        running = true;

        $info(taskCheck + "任务开始");

        startup();

        $info(taskCheck + "任务结束");

        running = false;
    }

    @Autowired
    protected TaskDao taskDao;

    /**
     * 获取 job 配置
     */
    protected List<TaskControlBean> getControls() {
        return taskDao.getTaskControlList(getTaskName());
    }

    public void startup() {
        // 先获取配置
        List<TaskControlBean> taskControlList = getControls();

        if (taskControlList.size() < 1) {
            return;
        }

        // 是否可以运行的判断
        if (!TaskControlUtils.isRunnable(taskControlList)) {
            return;
        }

        String taskID = TaskControlUtils.getTaskId(taskControlList);

        TaskControlEnums.Status status = TaskControlEnums.Status.START;

        // 任务监控历史记录添加:启动
        taskDao.insertTaskHistory(taskID, status.getIs());

        try {
            getTaskService().onStartup(taskControlList);
            status = TaskControlEnums.Status.SUCCESS;

        } catch (BusinessException be) {
            status = TaskControlEnums.Status.ERROR;
        } catch (Exception e) {
            status = TaskControlEnums.Status.ERROR;
        }

        // 任务监控历史记录添加:结束
        taskDao.insertTaskHistory(taskID, status.getIs());
    }
}
