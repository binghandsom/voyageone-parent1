package com.voyageone.components.rabbitmq.annotation;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RabbitListener
public @interface VOSubRabbitListener {
}
