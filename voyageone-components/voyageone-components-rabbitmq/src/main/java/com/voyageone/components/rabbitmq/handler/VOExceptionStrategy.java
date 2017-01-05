package com.voyageone.components.rabbitmq.handler;
import com.voyageone.components.rabbitmq.exception.MQException;
import com.voyageone.components.rabbitmq.exception.MQIgnoreException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.rabbitmq.service.IMqBackupMessage;
import com.voyageone.components.rabbitmq.utils.MQControlHelper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 致命异常判定策略
 *
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VOExceptionStrategy extends ComponentBase implements FatalExceptionStrategy {

    public static final String CONSUMER_RETRY_KEY = "consumerRetryTimes";

    public static final int MAX_RETRY_TIMES = 3;

    @Autowired
    private IMqBackupMessage mqBackMessageService;


    /**
     * 是否致命判定
     *
     * @param t throwable
     * @return 是否致命
     */
    @Override
    public boolean isFatal(Throwable t) {
        /** listener执行异常，并且异常cause为MessageConversionException，判定致命，此段逻辑兼容mq默认版本 **/
        if (t instanceof ListenerExecutionFailedException && t.getCause() instanceof MessageConversionException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Fatal message conversion error; message rejected; "
                        + "it will be dropped or routed to a dead letter exchange, if so configured: "
                        + ((ListenerExecutionFailedException) t).getFailedMessage(), t);
            }
            return true;
        }
        /** ignore异常，忽略消息，判定致命 **/
        if (t.getCause() instanceof MQIgnoreException) {
            return true;
        }
        /** 其他异常，异步线程备份数据到数据库，主线程判定致命 **/
        if (t.getCause() instanceof MQException) {
            new Thread(() -> validateMsg(((MQException) t.getCause()).getMqMessage())).start();
        }
        return true;
    }

    /**
     * 使mqmsg合法化
     *
     * @param message mqmsg
     */
    private void validateMsg(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        try {
            /* 插入数据库 */
            Map<String, Object> msgMap = JacksonUtil.jsonToMap(new String(message.getBody(), "UTF-8"));
            // RETRY>3 return
            if (MQControlHelper.isOutRetryTimes(msgMap)) { //CONSUMER_RETRY_KEY > 3
                return; //不做任何处理
            }


            /* 加入CONSUMER_RETRY_KEY */
            msgMap.put(CONSUMER_RETRY_KEY, StringUtils.isEmpty(msgMap.get(CONSUMER_RETRY_KEY)) ? 1 : (int) msgMap.get(CONSUMER_RETRY_KEY) + 1);

            mqBackMessageService.addBackMessage(messageProperties.getReceivedRoutingKey(), msgMap);
        } catch (UnsupportedEncodingException e) {
            logger.error("rabbitmq listener error-handle exception", e);
        }
    }

}
