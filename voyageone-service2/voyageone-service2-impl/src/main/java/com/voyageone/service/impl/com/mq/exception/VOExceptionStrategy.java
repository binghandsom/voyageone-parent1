package com.voyageone.service.impl.com.mq.exception;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.com.mq.MqSender;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class VOExceptionStrategy implements FatalExceptionStrategy {

    private static final String CONSUMER_RETRY_KEY= "$consumer_retry_times$";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MqSender sender;

    /**
     * 是否致命判定
     * @param t throwable
     * @return 是否致命
     */
    @Override
    public boolean isFatal(Throwable t) {
        /** listener执行异常，并且异常cause为MessageConversionException，判定致命，此段逻辑兼容mq默认版本 **/
        if (t instanceof ListenerExecutionFailedException
                && t.getCause() instanceof MessageConversionException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Fatal message conversion error; message rejected; "
                        + "it will be dropped or routed to a dead letter exchange, if so configured: "
                        + ((ListenerExecutionFailedException) t).getFailedMessage(), t);
            }
            return true;
        }
        /** ignore异常，忽略消息，判定致命 **/
        if(t.getCause() instanceof MQIgnoreException) return true;
        /** 其他异常，异步线程备份数据到数据库，主线程判定致命 **/
        if(t.getCause() instanceof MQException) new Thread(()->validateMsg(((MQException) t.getCause()).getMqMessage())).start();
        return true;
    }

    /**
     * 使mqmsg合法化
     * @param message mqmsg
     */
    private void validateMsg(Message message){
        MessageProperties messageProperties=message.getMessageProperties();
        try {
            Map<String,Object> headers=messageProperties.getHeaders();
            if(!MapUtils.isEmpty(headers)&& //headers非空
                    !StringUtils.isEmpty(headers.get(CONSUMER_RETRY_KEY))&& //CONSUMER_RETRY_KEY非空
                    (Integer.parseInt(headers.get(CONSUMER_RETRY_KEY).toString())>3)) { //CONSUMER_RETRY_KEY > 3
                return; //不做任何处理
            }
            /* 插入数据库 */
            Map<String,Object> msgMap=JacksonUtil.jsonToMap(new String(message.getBody(),"UTF-8"));
            /* 加入CONSUMER_RETRY_KEY */
            msgMap.put(CONSUMER_RETRY_KEY,StringUtils.isEmpty(headers.get(CONSUMER_RETRY_KEY))?1:1+Integer.parseInt(headers.get(CONSUMER_RETRY_KEY).toString()));
            sender.addBackMessage(messageProperties.getReceivedRoutingKey(),msgMap);
        } catch (UnsupportedEncodingException e) {
            logger.error("rabbitmq listener error-handle exception",e);
        }
    }

}
