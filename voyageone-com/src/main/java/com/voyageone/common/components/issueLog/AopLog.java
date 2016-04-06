package com.voyageone.common.components.issueLog;

import com.voyageone.common.util.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author aooer 2016/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
public class AopLog{

    public void doBefore(JoinPoint joinPoint) {
        getLog(joinPoint).info(getMethodPath(joinPoint)+"\tStart");
    }

    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        getLog(joinPoint).error("异常方法:" + getMethodPath(joinPoint));
        getLog(joinPoint).error("异常参数:" + JacksonUtil.bean2Json(joinPoint.getArgs()));
        getLog(joinPoint).error("异常信息:", e);
    }

    public void doAfter(JoinPoint joinPoint) {
        getLog(joinPoint).info(getMethodPath(joinPoint)+"\tend");
    }

    private Logger aopLoger = null;
    private Logger getLog(JoinPoint joinPoint) {
        if (aopLoger == null) {
            aopLoger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        }
        return aopLoger;
    }

    private String getMethodPath(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
    }
}
