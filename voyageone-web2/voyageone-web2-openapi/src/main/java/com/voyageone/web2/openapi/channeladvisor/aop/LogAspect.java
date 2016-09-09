package com.voyageone.web2.openapi.channeladvisor.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.JacksonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
@Aspect
public class LogAspect extends VOAbsLoggable {

    private static final String POINT_CUT = "execution(public * com.voyageone.web2.openapi.channeladvisor.control.*Controller.*(..))";

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    final RateLimiter rateLimiter = RateLimiter.create(100000.0,1, TimeUnit.MILLISECONDS);

    @Around(POINT_CUT)
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        rateLimiter.tryAcquire();
        ServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        $info(pjp.getSignature().getName()+":::"+JacksonUtil.bean2Json(request.getParameterMap()));
        return pjp.proceed();
    }

    @AfterThrowing(value = POINT_CUT, throwing = "e")
    public void doAfterThrow(JoinPoint jp, Exception e) {
        log.error("发生异常：" + jp.getSignature().getName(), e);
    }

}
