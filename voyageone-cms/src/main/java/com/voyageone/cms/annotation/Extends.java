package com.voyageone.cms.annotation;

import java.lang.annotation.*;

/**
 * 标注bean里面的属性是不是有继承关系
 * @author james
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)  
public @interface Extends {
	
}
