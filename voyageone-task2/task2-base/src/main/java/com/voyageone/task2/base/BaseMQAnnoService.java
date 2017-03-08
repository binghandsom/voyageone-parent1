package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQRunnable;
import com.voyageone.components.rabbitmq.annotation.VOMQStart;
import com.voyageone.components.rabbitmq.annotation.VOMQStop;
import com.voyageone.components.rabbitmq.exception.MQException;
import com.voyageone.components.rabbitmq.exception.MQIgnoreException;
import com.voyageone.components.rabbitmq.handler.VOExceptionStrategy;
import com.voyageone.components.rabbitmq.utils.MQControlHelper;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.support.SimpleAmqpHeaderMapper;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础 Message Queue任务 服务类
 * <p>
 * Created by jonas on 15/6/6.
 */
public abstract class BaseMQAnnoService extends BaseTaskService {

    /**
     * taskControlList job配置
     */
    protected List<TaskControlBean> taskControlList = null;

    /**
     * 默认公开的启动入口
     */
    @Override
    public void startup() {}

    /**
     * RabbitHandler
     */
    @RabbitHandler
    protected void onMessage(byte[] message, @Headers Map<String, Object> headers) throws Exception {
        MDC.put("taskName", getTaskName());
        MDC.put("subSystem", getSubSystem().name().toLowerCase());

        SimpleAmqpHeaderMapper headerMapper = new SimpleAmqpHeaderMapper();
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        MessageProperties messageProperties = new MessageProperties();
        headerMapper.fromHeaders(messageHeaders, messageProperties);

        //监听通知消息，执行任务
        onMessage(new Message(message, messageProperties));

        MDC.remove("taskName");
        MDC.remove("subSystem");
    }

    /**
     * 监听通知消息，执行任务
     *
     * @param message 接受到的消息体
     */
    private void onMessage(Message message) {
        //先获取配置
        initControls();
        startup(message);
    }

    /**
     * 获取配置
     */
    private void initControls() {
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

    /**
     * 默认公开的启动入口
     */
    public void startup(Message message) {
        String messageStr = "";
        try {
            messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
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
    }

    /**
     * MqJobService需要实现此方法
     *
     * @param messageMap Mq消息Map
     */
    protected abstract void onStartup(Map<String, Object> messageMap) throws Exception;

    /**
     * 是否超过重试次数的判断
     */
    private boolean isOutRetryTimes(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        // RETRY>3 return
        return !MapUtils.isEmpty(headers) && //headers非空
                !StringUtils.isEmpty(headers.get(VOExceptionStrategy.CONSUMER_RETRY_KEY)) && //CONSUMER_RETRY_KEY非空
                (int) headers.get(VOExceptionStrategy.CONSUMER_RETRY_KEY) >= VOExceptionStrategy.MAX_RETRY_TIMES;
    }

    /**
     * 判断TASK是否可以执行
     */
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

    /**
     * 执行启动TASK
     */
    @VOMQStart
    public boolean startMQ() {
        try {
            MQControlHelper.start(getEndPointId());
            // set concurrentConsumers
            String threadCount = null;
            if (taskControlList != null) {
                threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.mq_thread_count);
            }
            int nThreads = StringUtils.isEmpty(threadCount) ? 1 : Integer.parseInt(threadCount);
            MQControlHelper.setConcurrentConsumers(this.getEndPointId(), nThreads);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 执行停止TASK
     */
    @VOMQStop
    public boolean stopMQ() {
        MQControlHelper.stop(this.getEndPointId());
        return true;
    }

}
