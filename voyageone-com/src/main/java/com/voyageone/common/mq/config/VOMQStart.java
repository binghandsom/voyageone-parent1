package com.voyageone.common.mq.config;

import java.lang.annotation.*;

/**
 * @author chuanyu.liang 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VOMQStart {

}
