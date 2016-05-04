package com.voyageone.base.dao.mongodb.support;

import java.lang.annotation.*;

/**
 * Created by Jonas on 2016/3/9.
 * @version 2.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MongoCollection {
    /**
     * The mongo collection name
     * @return the mongo collection name, if the class name is not equals mongo
     */
    String value() default "";
}
