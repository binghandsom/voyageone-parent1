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
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();

            String user = "admin";


            Object[] arguments = joinPoint.getArgs();


            String ip = request.getRemoteAddr();
            String url = request.getRequestURI();
            String application = "admin";

            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            String clsAndMethod =  className + "." + methodName;
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;

            Map<String, Object> map = new HashMap<>();
            map.put("application", application);
            map.put("ip", ip);
            map.put("url", url);
            map.put("action", clsAndMethod);
            map.put("request", arguments);
            map.put("response", result);
            map.put("executionTime", elapsedTime);
            map.put("creater", user);

            log.info(JacksonUtil.bean2Json(map));

            return result;
        } catch (IllegalArgumentException e) {
//            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
//                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }

//    public static Map<String, Object> getParmMap(HttpServletRequest request) {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//
//        Map<String, String[]> orimap = request.getParameterMap();
//        Set<String> keys = orimap.keySet();
//        for (String key1 : keys) {
//            String key = key1;
//            String[] value = orimap.get(key);
//            if (value.length > 1) {
//                map.put(key, value);
//            } else {
//                map.put(key, value[0]);
//            }
//        }
//        return map;
//    }

}