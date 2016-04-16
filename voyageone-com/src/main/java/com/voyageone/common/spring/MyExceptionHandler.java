package com.voyageone.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by xyyz150 on 2014/11/11.
 */
public class MyExceptionHandler implements HandlerExceptionResolver,Ordered {
    public static Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
    private int order = Ordered.LOWEST_PRECEDENCE;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex){
        Throwable throwable=ex;
        while(throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) ex).getTargetException();
        }
        logger.error("resolveException",throwable);
        //如果是json格式的ajax请求
        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
            response.setStatus(500);
            response.setContentType("application/json;charset=utf-8");
            try {
                String msg=throwable.getMessage();
                if(msg==null||msg.equals(""))
                {
                    msg=throwable.toString();
                }
                msg+=throwable.getStackTrace();
                response.getWriter().write(msg);
                response.getWriter().flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return new ModelAndView();
        }
        else{//如果是普通请求
            String msg=throwable.getMessage();
            if(msg==null||msg.equals(""))
            {
                msg=ex.toString();
            }
            msg+=throwable.getStackTrace();
            request.setAttribute("exceptionMessage", msg);
            return new ModelAndView("error");
        }
    }
}


