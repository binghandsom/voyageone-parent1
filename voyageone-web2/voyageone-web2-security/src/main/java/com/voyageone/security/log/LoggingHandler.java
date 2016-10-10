package com.voyageone.security.log;

import com.voyageone.common.util.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.apache.shiro.SecurityUtils;
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


@Aspect
@Component
public class LoggingHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.voyageone.web2.admin.*.*.*Controller.*(..))")
    protected void controller() {
    }

    @Pointcut("execution(public * com.voyageone.web2.admin.*.*.*Controller.get*(..))")
    protected void getXXX() {
    }

    @Pointcut("execution(public * com.voyageone.web2.admin.*.*.*Controller.search*(..))")
    protected void searchXXX() {
    }
    @Pointcut("execution(public * com.voyageone.web2.admin.*.*.*Controller.init(..))")
    protected void initXXX() {
    }

    @Around("controller() && !getXXX() && !searchXXX() && !initXXX()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip)) {
        	ip = request.getRemoteAddr();
        }
        String url = request.getRequestURI();
        String application = getApp(url);
        String user = SecurityUtils.getSubject().getPrincipal().toString();
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String clsAndMethod =  className + "." + methodName;
//        Map<String, Object> map = new HashMap<>();
        MDC.put("application", application);
        MDC.put("ip", ip);
        MDC.put("url", url);
        MDC.put("action", clsAndMethod);
        Object ObjRequest = (arguments == null || arguments.length ==0) ? null : arguments[0] == null ? null :arguments[0] ;
        if(ObjRequest!= null) {
            MDC.put("request", ObjRequest);
        }
        MDC.put("creater", user);
        try {
//            HttpSession session = request.getSession();
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            MDC.put("response", result);
            MDC.put("executionTime", elapsedTime);

            log.info(JacksonUtil.bean2Json("write"));

            return result;
        } catch (Exception e) {
            MDC.put("response", e.getStackTrace());
            MDC.put("executionTime", 0);
            log.error(JacksonUtil.bean2Json("write"));
            throw e;
        }
    }

    private String getApp(String url)
    {
        if(StringUtils.isEmpty(url))
            return "";

        String [] a = url.split("/+");

        for (String str : a) {
            if(!StringUtils.isEmpty(url))
            {
                return str;
            }
        }
        return "";
    }

}