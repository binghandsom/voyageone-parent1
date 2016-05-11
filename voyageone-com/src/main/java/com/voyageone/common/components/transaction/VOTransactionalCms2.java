package com.voyageone.common.components.transaction;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * VoyageOne 事务注解
 * @author aooer 2016/3/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor=Exception.class)
public @interface VOTransactionalCms2 {
    @AliasFor("transactionManager")
    String value() default "transactionManagerCms2";

    @AliasFor("value")
    String transactionManager() default "transactionManagerCms2";

    Propagation propagation() default Propagation.REQUIRED;

    Isolation isolation() default Isolation.DEFAULT;

    int timeout() default -1;

    boolean readOnly() default false;

}
