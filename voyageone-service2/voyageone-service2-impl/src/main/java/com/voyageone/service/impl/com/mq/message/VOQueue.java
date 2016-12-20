package com.voyageone.service.impl.com.mq.message;

import java.lang.annotation.*;

/**
 * Created by dell on 2016/12/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VOQueue {
    String[] queues() default {};
}
