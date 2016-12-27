package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.components.rabbitmq.annotation.VOMQRunnable;
import com.voyageone.components.rabbitmq.annotation.VOMQStart;
import com.voyageone.components.rabbitmq.annotation.VOMQStop;
import com.voyageone.components.rabbitmq.exception.MQException;
import com.voyageone.components.rabbitmq.exception.MQIgnoreException;
import com.voyageone.common.util.GenericSuperclassUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.service.IMQJobLog;
import com.voyageone.components.rabbitmq.service.IVOMQOnStartup;
import com.voyageone.components.rabbitmq.utils.MQControlHelper;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.log4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.support.SimpleAmqpHeaderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基础 Message Queue任务 服务类
 * <p>
 * Created by jonas on 15/6/6.
 */
public abstract class TBaseMQAnnoService<TMQMessageBody extends IMQMessageBody> extends BaseTaskService implements IVOMQOnStartup<TMQMessageBody> {

    @Autowired()
    IMQJobLog mqJobLogService;
    /**
     * taskControlList job配置
     */
    protected List<TaskControlBean> taskControlList = null;

    /**
     * 默认公开的启动入口
     */
    @Override
    public void startup() {
    }

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
            //获取泛型的真实类型class
            Class<TMQMessageBody> messageBodyClass = GenericSuperclassUtils.getGenericActualTypeClass(this);

            TMQMessageBody messageBody = JacksonUtil.json2Bean(messageStr, messageBodyClass);

            startup(messageBody);
        } catch (BusinessException be) {
            $error("出现业务异常，任务退出", be);
            throw new MQIgnoreException(be);
        } catch (MQIgnoreException me) {
            $error("MQIgnoreException，任务退出", me);
            throw new MQIgnoreException(me);
        } catch (Exception ex) {
            if (MQControlHelper.isOutRetryTimes(message)) {
                logIssue(ex, ex.getMessage() + messageStr);
            }
            $error("出现异常，任务退出", ex);
            throw new MQException(ex, message);
        }
    }

    //是否记录Job日志
    public boolean isMQJobLog() {
        //  cfg_name = 'run_flg'  cfg_val2=1 全量记录mq处理日志   默认只记录异常日志
        String val2 = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.run_flg, "1");
        return "1".equals(val2);
    }

    public void startup(TMQMessageBody messageBody) throws Exception {
        Date beginDate = new Date();
        try {
            onStartup(messageBody);
            if (isMQJobLog()) {//记录mq日志
                log(messageBody, null, beginDate);
            }
        }
        //特殊异常 抛出处理
        catch (Exception ex) {

            log(messageBody, ex, beginDate);
            throw ex;
        }
    }

    private void log(IMQMessageBody messageBody, Exception ex, Date beginDate) {

        mqJobLogService.log(messageBody, ex, beginDate, new Date());
    }
    /**
     * MqJobService需要实现此方法
     *
     * @param messageBody Mq消息Map
     */
    public abstract void onStartup(TMQMessageBody messageBody) throws Exception;

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
            // flush taskControlList
            taskControlList = getControls();
            if (taskControlList == null) {
                taskControlList = new ArrayList<>();
            }
            MQControlHelper.start(getClass().getName());
            // set concurrentConsumers
            String threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.mq_thread_count);
            int nThreads = StringUtils.isEmpty(threadCount) ? 1 : Integer.parseInt(threadCount);
            MQControlHelper.setConcurrentConsumers(getClass().getName(), nThreads);
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
        MQControlHelper.stop(getClass().getName());
        taskControlList = null;
        return true;
    }

}
