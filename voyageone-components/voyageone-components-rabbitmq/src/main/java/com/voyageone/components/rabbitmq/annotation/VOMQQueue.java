package com.voyageone.components.rabbitmq.annotation;

import java.lang.annotation.*;

/**
 * Created by dell on 2016/12/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VOMQQueue {
    /**
     * Queue routingKey[队列KEY]
     */
    String value();

    /**
     * Queue exchange[队列交换机]
     */
    String exchange() default "";

    /**
     * 出错时是否把消息保存在数据库中，以后会自动发送到消息中 [true: try catch; false:throw exception]
     */
    boolean isBackMessage() default true;

    /**
     * 是否检测消息定义存在
     */
    boolean isDeclareQueue() default true;

    /**
     * 延迟发送时间(秒)
     */
    long delaySecond() default 0;
}
