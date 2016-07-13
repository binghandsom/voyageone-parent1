package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.mq.config.VOMQRunnable;
import com.voyageone.common.mq.config.VOMQStart;
import com.voyageone.common.mq.config.VOMQStop;
import com.voyageone.common.mq.exception.MQException;
import com.voyageone.common.mq.exception.MQIgnoreException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.com.mq.MQControlHelper;
import com.voyageone.service.impl.com.mq.handler.VOExceptionStrategy;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.support.SimpleAmqpHeaderMapper;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BaseMQAnnoService
 *
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseMQAnnoService extends BaseTaskService {

    //taskControlList job配置
    protected List<TaskControlBean> taskControlList = null;

    /**
     * @param taskControlList job 配置
     * @throws Exception
     * @deprecated ignore MqJobService不需要实现此方法
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        throw new Exception("not support!");
    }

    /**
     * MqJobService需要实现此方法
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    public abstract void onStartup(Map<String, Object> messageMap) throws Exception;

    @RabbitHandler
    protected void onMessage(byte[] message, @Headers Map<String, Object> headers) throws Exception {
        SimpleAmqpHeaderMapper headerMapper = new SimpleAmqpHeaderMapper();
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        MessageProperties messageProperties = new MessageProperties();
        headerMapper.fromHeaders(messageHeaders, messageProperties);

        onMessage(new Message(message, messageProperties));
    }

    // 先获取配置
    protected void initControls() {
        if (taskControlList == null) {
            taskControlList = getControls();
            if (taskControlList == null) {
                taskControlList = new ArrayList<>();
            }
        }
        if (taskControlList.isEmpty()) {
            $info("没有找到任何配置。");
            logIssue("没有找到任何配置！！！", getTaskName());
        }
    }

    @VOMQRunnable
    public boolean isRunnable() {
        //先获取配置
        initControls();
        try {
            if (!taskControlList.isEmpty()) {
                return TaskControlUtils.isRunnable(taskControlList, true);
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @VOMQStart
    public boolean startMQ() {
        try {
            MQControlHelper.start(getClass().getName());
            // set concurrentConsumers
            String threadCount = null;
            if (taskControlList != null) {
                threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.mq_thread_count);
            }
            int nThreads = StringUtils.isEmpty(threadCount) ? 1 : Integer.parseInt(threadCount);
            MQControlHelper.setConcurrentConsumers(getClass().getName(), nThreads);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @VOMQStop
    public boolean stopMQ() {
        MQControlHelper.stop(getClass().getName());
        return true;
    }

    /**
     * 监听通知消息，执行任务
     *
     * @param message 接受到的消息体
     */
    protected void onMessage(Message message) {
        //先获取配置
        initControls();
        process(message);
    }

    private TaskControlEnums.Status process(Message message) {
        String messageStr = "";
        try {
            messageStr = new String(message.getBody(), "UTF-8");
            Map<String, Object> messageMap = JacksonUtil.jsonToMap(messageStr);
            onStartup(messageMap);
        } catch (BusinessException be) {
            $error("出现业务异常，任务退出", be);
            throw new MQIgnoreException(be);
        } catch (MQIgnoreException me) {
            $error("MQIgnoreException，任务退出", me);
            throw new MQIgnoreException(me);
        } catch (Exception ex) {
            if (isOutRetryTimes(message)) {
                logIssue(ex, ex.getMessage() + messageStr);
            }
            $error("出现异常，任务退出", ex);
            throw new MQException(ex, message);
        }
        return TaskControlEnums.Status.SUCCESS;
    }

    private boolean isOutRetryTimes(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        // RETRY>3 return
        return !MapUtils.isEmpty(headers) && //headers非空
                !StringUtils.isEmpty(headers.get(VOExceptionStrategy.CONSUMER_RETRY_KEY)) && //CONSUMER_RETRY_KEY非空
                (int) headers.get(VOExceptionStrategy.CONSUMER_RETRY_KEY) >= VOExceptionStrategy.MAX_RETRY_TIMES;
    }


    public TaskControlBean getTaskControlBean(List<TaskControlBean> taskControlList, String cfg_name) {
        for (TaskControlBean taskControlBean : taskControlList) {
            if (taskControlBean.getCfg_name().equals(cfg_name)) {
                return taskControlBean;
            }
        }
        return null;
    }

}
