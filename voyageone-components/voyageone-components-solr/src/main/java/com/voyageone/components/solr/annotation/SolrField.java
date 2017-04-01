package com.voyageone.components.solr.annotation;

import java.lang.annotation.*;

/**
 * Created by james on 2017/3/28.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SolrField {
    String type() default "";
    String fieldType() default "field";
    String name() default "";
    boolean required() default false;
    boolean isIndex() default true;
}
