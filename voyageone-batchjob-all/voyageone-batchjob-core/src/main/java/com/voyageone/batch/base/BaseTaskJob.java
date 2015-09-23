package com.voyageone.batch.base;

import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础 Job 类
 *
 * Created by jonas on 15/6/6.
 */
public abstract class BaseTaskJob {

    private final Log logger = LogFactory.getLog(getClass());

    private boolean running = false;

    @Autowired
    protected IssueLog issueLog;

    protected Log getLogger() {
        return logger;
    }

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
            getLogger().info(taskCheck + "正在运行，忽略");
            return;
        }

        running = true;

        logger.info(taskCheck + "任务开始");

        getTaskService().startup();

        getLogger().info(taskCheck + "任务结束");

        running = false;
    }
}
