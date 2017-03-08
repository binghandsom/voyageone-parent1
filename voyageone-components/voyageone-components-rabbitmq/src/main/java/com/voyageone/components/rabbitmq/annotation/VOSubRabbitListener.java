package com.voyageone.components.rabbitmq.annotation;

import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MessageMapping
@Documented
public @interface VOSubRabbitListener {

    String id() default "";

    String containerFactory() default "";

    String[] queues() default {};

    boolean exclusive() default false;

    String priority() default "";

    String admin() default "";

    QueueBinding[] bindings() default {};

    String group() default "";
}
