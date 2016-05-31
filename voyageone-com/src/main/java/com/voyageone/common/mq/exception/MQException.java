package com.voyageone.common.mq.exception;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQException extends AmqpException {

    private final Message mqMessage;

    public Message getMqMessage() {
        return mqMessage;
    }

    public MQException(Throwable cause, Message mqMessage) {
        super(cause);
        this.mqMessage = mqMessage;
    }

}
