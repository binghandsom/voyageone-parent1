package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.logger.VOAbsIssueLoggable;

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

        getTaskService().startup();

        $info(taskCheck + "任务结束");

        running = false;
    }
}
