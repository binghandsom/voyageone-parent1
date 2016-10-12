package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.logger.VOAbsIssueLoggable;
import org.apache.log4j.MDC;

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

        String taskName = getTaskName();
        MDC.put("taskName", taskName);
        MDC.put("subSystem", getSubSystem().name().toLowerCase());

        if (running) {
            $info(taskName + "正在运行，忽略");
            return;
        }

        running = true;

        $info(taskName + "任务开始");

        startup();

        $info(taskName + "任务结束");

        running = false;
        MDC.remove("taskName");
        MDC.remove("subSystem");
    }

    public void startup() {
        getTaskService().startup();
    }
}
