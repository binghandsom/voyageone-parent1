package com.voyageone.common.mq.config;

import java.lang.annotation.*;

/**
 * Created by dell on 2016/12/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VOMQQueue {
    String[] queues();//default {}
}
