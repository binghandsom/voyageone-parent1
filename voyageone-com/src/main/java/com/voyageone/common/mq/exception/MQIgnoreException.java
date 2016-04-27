package com.voyageone.common.mq.exception;

import org.springframework.amqp.AmqpException;

/**
 * 忽略MQ异常，此种异常不做重试retry，直接从MQ消费掉
 *
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MQIgnoreException extends AmqpException {

    public MQIgnoreException(String message) {
        super(message);
    }

    public MQIgnoreException(Throwable cause) {
        super(cause);
    }

    public MQIgnoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
