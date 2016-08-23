package com.voyageone.security.log;

import com.voyageone.common.util.JacksonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    public void controller() {
//    }

    @Pointcut("execution(public * com.voyageone.web2.admin.*.*.*Controller.*(..))")
    protected void controller() {
    }

    @Around("controller() ")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        String application = "admin";
        String user = "admin";
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String clsAndMethod =  className + "." + methodName;
        Map<String, Object> map = new HashMap<>();
        map.put("application", application);
        map.put("ip", ip);
        map.put("url", url);
        map.put("action", clsAndMethod);
        map.put("request", arguments);
        map.put("creater", user);
        try {
//            HttpSession session = request.getSession();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            map.put("response", result);
            map.put("executionTime", elapsedTime);

            log.info(JacksonUtil.bean2Json(map));

            return result;
        } catch (Exception e) {
            map.put("response", e.getStackTrace());
            map.put("executionTime", 0);
            log.error(JacksonUtil.bean2Json(map));
            throw e;
        }
    }

}