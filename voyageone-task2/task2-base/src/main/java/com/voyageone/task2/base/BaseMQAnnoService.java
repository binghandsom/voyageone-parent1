package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.com.mq.exception.MQException;
import com.voyageone.service.impl.com.mq.exception.MQIgnoreException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.amqp.core.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMQAnnoService extends BaseTaskService {

    //taskControlList job配置
    protected List<TaskControlBean> taskControlList = null;

    /**
     * @deprecated
     * ignore MqJobService不需要实现此方法
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        throw new Exception("not support!");
    }

    /**
     * MqJobService需要实现此方法
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    public abstract void onStartup(Map<String, Object> messageMap) throws Exception;

    /**
     * 监听通知消息，执行任务
     * @param message 接受到的消息体
     */
    protected void onMessage(Message message) {
        // 先获取配置
        if (taskControlList == null) {
            taskControlList = getControls();
            if (taskControlList == null) {
                taskControlList = new ArrayList<>();
            }
        }

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

        TaskControlEnums.Status status = TaskControlEnums.Status.START;

        // 任务监控历史记录添加:启动
        taskDao.insertTaskHistory(taskID, status.getIs());

        try {
            String messageStr = new String(message.getBody(), "UTF-8");
            Map<String, Object> messageMap = JacksonUtil.jsonToMap(messageStr);

            onStartup(messageMap);
            status = TaskControlEnums.Status.SUCCESS;

        } catch (BusinessException be) {
            logIssue(be, be.getInfo());
            status = TaskControlEnums.Status.ERROR;
            $error("出现业务异常，任务退出", be);
            throw new MQIgnoreException(be);
        }  catch (Exception ex) {
            logIssue(ex);
            status = TaskControlEnums.Status.ERROR;
            $error("出现异常，任务退出", ex);
            MQException mqException = new MQException(ex);
            mqException.setMqMessage(message);
            throw mqException;
        } finally {
            // 任务监控历史记录添加:结束
            taskDao.insertTaskHistory(taskID, status.getIs());
        }
    }

}
