package com.voyageone.batch.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.core.Enums.TaskControlEnums.Status;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;

import java.util.List;
import java.util.Map;

/**
 * MqJob服务基类
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMQTaskService extends BaseTaskService{

    /**
     * @deprecated
     * ignore MqJobService不需要实现此方法
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {}

    /**
     * MqJobService需要实现此方法
     * @param taskControlList job配置
     * @param message Mq消息
     * @throws Exception
     */
    protected abstract void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception;

    /**
     * @deprecated
     * ignore MqJobService不需要关注此方法
     */
    @Override
    public void startup(){};

    /**
     * 默认公开的启动入口
     */
    public void startup(Map<String,Object> messageMap) {
        // 先获取配置
        List<TaskControlBean> taskControlList = getControls();

        if (taskControlList.size() < 1) {
            $info("没有找到任何配置。");
            logIssue("没有找到任何配置！！！", getTaskName());
            return;
        }

        // 是否可以运行的判断
        if (!TaskControlUtils.isRunnable(taskControlList)) {
            return;
        }

        String taskID = TaskControlUtils.getTaskId(taskControlList);

        Status status = Status.START;

        // 任务监控历史记录添加:启动
        taskDao.insertTaskHistory(taskID, status.getIs());

        try {
            onStartup(taskControlList, messageMap);
            status = Status.SUCCESS;

        } catch (BusinessException be) {
            logIssue(be, be.getInfo());
            status = Status.ERROR;
            $info("出现业务异常，任务退出");
        } catch (Exception e) {
            logIssue(e);
            status = Status.ERROR;
            logger.error("出现异常，任务退出", e);
        }

        // 任务监控历史记录添加:结束
        taskDao.insertTaskHistory(taskID, status.getIs());
    }
}