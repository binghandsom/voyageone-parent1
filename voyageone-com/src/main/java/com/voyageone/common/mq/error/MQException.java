package com.voyageone.common.mq.error;

import org.springframework.amqp.core.Message;

/**
 *
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQException extends RuntimeException {


    private Message mqMessage;

    public Message getMqMessage() {
        return mqMessage;
    }

    public void setMqMessage(Message mqMessage) {
        this.mqMessage = mqMessage;
    }

    public MQException(String message) {
        super(message);
    }

    public MQException(Throwable cause) {
        super(cause);
    }

    public MQException(String message, Throwable cause) {
        super(message, cause);
    }
}
