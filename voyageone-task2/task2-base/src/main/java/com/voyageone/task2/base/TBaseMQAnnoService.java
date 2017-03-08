package com.voyageone.task2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.GenericSuperclassUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOMQRunnable;
import com.voyageone.components.rabbitmq.annotation.VOMQStart;
import com.voyageone.components.rabbitmq.annotation.VOMQStop;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanName;
import com.voyageone.components.rabbitmq.exception.MQException;
import com.voyageone.components.rabbitmq.exception.MQIgnoreException;
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
public abstract class TBaseMQAnnoService<TMQMessageBody extends IMQMessageBody> extends BaseTaskService implements IVOMQOnStartup<TMQMessageBody>, IMQSubBeanName {

    /**
     * MQ 任务拆分后的子Bean名称
     */
    private String subBeanName;

    @Override
    public String getSubBeanName() {
        return subBeanName;
    }

    @Override
    public void setSubBeanName(String subTaskName) {
        this.subBeanName = subTaskName;
    }

    /**
     * taskControlList job配置
     */
    protected List<TaskControlBean> taskControlList = null;

    @Autowired
    protected IMQJobLog mqJobLogService;

    /**
     * 默认公开的启动入口
     */
    @Override
    public void startup() {
    }

    /**
     * onMessage
     *
     * @param message message
     * @param headers headers
     * @throws Exception Exception
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
            $warn("没有找到任何配置。");
            logIssue("没有找到任何配置！！！", getTaskName());
        }
    }

    /**
     * 是否记录Job日志
     *
     * @return true: 记录, false: 不记录
     */
    private boolean isMQJobLog() {
        //  cfg_name = 'run_flg'  cfg_val2=1 全量记录mq处理日志   默认只记录异常日志
        String val2 = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.run_flg, "1");

        logger.info(" RUN FLAG  val2为" + val2);
        return "1".equals(val2);
    }

    /**
     * 启动Mq
     *
     * @param messageBody messageBody
     * @throws Exception Exception
     */
    private void doStartup(TMQMessageBody messageBody) throws Exception {
        Date beginDate = new Date();
        try {
            startup(messageBody);
            if (isMQJobLog()) {//记录mq日志
                log(messageBody, null, beginDate);
            }
        } catch (Exception ex) {

            log(messageBody, ex, beginDate);
            throw ex;
        }
    }

    /**
     * 记录履历
     *
     * @param messageBody messageBody
     * @param ex          ex
     * @param beginDate   beginDate
     */
    private void log(IMQMessageBody messageBody, Exception ex, Date beginDate) {

        mqJobLogService.log(this.getTaskName(), messageBody, ex, beginDate, new Date());
    }

    /**
     * 默认公开的启动入口
     *
     * @param message message
     */
    public void startup(Message message) {
        String messageStr = "";
        try {
            messageStr = new String(message.getBody(), StandardCharsets.UTF_8);
            //获取泛型的真实类型class
            Class<TMQMessageBody> messageBodyClass = GenericSuperclassUtils.getGenericActualTypeClass(this);

            TMQMessageBody messageBody = JacksonUtil.json2Bean(messageStr, messageBodyClass);

            doStartup(messageBody);
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

    /**
     * @param messageBody messageBody
     * @throws Exception Exception
     */
    public void startup(TMQMessageBody messageBody) throws Exception {
        onStartup(messageBody);
    }

    /**
     * MqJobService需要实现此方法
     *
     * @param messageBody Mq消息Map
     */
    public abstract void onStartup(TMQMessageBody messageBody) throws Exception;

    /**
     * 判断TASK是否可以执行
     *
     * @return true: 可执行, false: 不可执行
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
     * 手动开启监听器
     *
     * @return true: 开启成功, false: 开启失败
     */
    @VOMQStart
    public boolean startMQ() {
        try {
            // flush taskControlList
            taskControlList = getControls();
            if (taskControlList == null) {
                taskControlList = new ArrayList<>();
            }
            MQControlHelper.start(this.getEndPointId());
            // set concurrentConsumers
            String threadCount = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.mq_thread_count);
            int nThreads = StringUtils.isEmpty(threadCount) ? 1 : Integer.parseInt(threadCount);
            MQControlHelper.setConcurrentConsumers(this.getEndPointId(), nThreads);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 手动关闭监听器
     *
     * @return true: 关闭成功
     */
    @VOMQStop
    public boolean stopMQ() {
        MQControlHelper.stop(this.getEndPointId());
        taskControlList = null;
        return true;
    }


}
