package com.voyageone.security.spring;

import com.voyageone.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Ethan Shi on 2016-08-22.
 */

@Deprecated

public class MyModelAndViewResolver extends ViewNameMethodReturnValueHandler implements ModelAndViewResolver, BeanPostProcessor, ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(MyModelAndViewResolver.class);
    /**
     * 支持处理多种参数逻辑
     */
    public static final ThreadLocal<Param> params = new ThreadLocal<Param>() {
        @Override
        protected Param initialValue() {
            return new Param();
        }
    };

    public ModelAndView resolveModelAndView(Method handlerMethod, Class handlerType, Object returnValue, ExtendedModelMap implicitModel, NativeWebRequest webRequest) {
        handlerMonitor(handlerMethod, returnValue, (ServletWebRequest) webRequest);
        return ModelAndViewResolver.UNRESOLVED;
    }

    private void handlerMonitor(Method handlerMethod, Object returnValue, ServletWebRequest webRequest) {
        Class<?> declaringClass = handlerMethod.getDeclaringClass();
        String clsAndMethod = declaringClass.getSimpleName() + "." + handlerMethod.getName();
        HttpServletRequest request = webRequest.getRequest();
        //获取请求ip
        Map<String, Object> parmMap = getParmMap(request);
        params.get().setP1(parmMap);
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        String application = "";

        if(url != null && !url.isEmpty())
        {
            application = url.substring(0, url.indexOf("/"));

            if(application == null || application.isEmpty())
            {
                application = "";
            }
        }


        Object retStr = returnValue;
        if (returnValue != null && !isPrimitive(returnValue.getClass()))
            retStr = JacksonUtil.bean2Json(returnValue);

        //此处改写到自己的日志输出器里或者其他地方

        Map<String, Object> map = new HashMap<>();
        map.put("application", application);
        map.put("ip", ip);
        map.put("url", url);
        map.put("action", clsAndMethod);
        map.put("request", params.get());
        map.put("response", returnValue);

        log.info(JacksonUtil.bean2Json(map));
        params.get().clear();
    }

    /**
     * 检查是否是基本类型
     *
     * @param cls
     * @return 是 基本 返回true，否则反之
     */
    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || cls == Boolean.class || cls == Byte.class || cls == Character.class || cls == Short.class || cls == Integer.class || cls == Long.class || cls == Float.class
                || cls == Double.class || cls == String.class || cls == Date.class || cls == Class.class;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        handlerMonitor(returnType.getMethod(), returnValue, (ServletWebRequest) webRequest);
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    /**
     * extract a Map<string,Object> from HttpServletRequest
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParmMap(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> orimap = request.getParameterMap();
        Set<String> keys = orimap.keySet();
        for (String key1 : keys) {
            String key = key1;
            String[] value = orimap.get(key);
            if (value.length > 1) {
                map.put(key, value);
            } else {
                map.put(key, value[0]);
            }
        }
        return map;
    }

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter rma = (RequestMappingHandlerAdapter) bean;
            //处理自定义messageConverters
            List<HttpMessageConverter<?>> messageConverters = rma.getMessageConverters();
            for (HttpMessageConverter<?> httpMessageConverter : messageConverters) {
                if (MappingJackson2HttpMessageConverter.class.isAssignableFrom(httpMessageConverter.getClass())) {
                    messageConverters.remove(httpMessageConverter);
                    MappingJackson2HttpMessageConverter bean2 = new MappingJackson2HttpMessageConverter() {
                        @Override
                        public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                            Object o = super.read(type, contextClass, inputMessage);
                            MyModelAndViewResolver.params.get().setP2(o);//set到 自定义的Params中去
                            return o;
                        }
                    };
                    messageConverters.add(bean2);
                    break;
                }
            }
            rma.setMessageConverters(messageConverters);
            //处理自定义的handler
            List<HandlerMethodReturnValueHandler> returnValueHandlers = rma.getReturnValueHandlers();
            List<HandlerMethodReturnValueHandler> newList = new ArrayList<HandlerMethodReturnValueHandler>();
            for (HandlerMethodReturnValueHandler returnValueHandler : returnValueHandlers) {
                if (ViewNameMethodReturnValueHandler.class.isAssignableFrom(returnValueHandler.getClass())) {
                    MyModelAndViewResolver bean2 = applicationContext.getBean(MyModelAndViewResolver.class);
                    newList.add(bean2);
                }
                if (RequestResponseBodyMethodProcessor.class.isAssignableFrom(returnValueHandler.getClass())) {
                    MyRequestResponseBodyMethodProcessor bean2 = new MyRequestResponseBodyMethodProcessor(rma.getMessageConverters());
                    newList.add(bean2);
                }
                newList.add(returnValueHandler);
            }
            rma.setReturnValueHandlers(newList);
        }
        if (bean instanceof MappingJackson2HttpMessageConverter) {
            MappingJackson2HttpMessageConverter bean2 = new MappingJackson2HttpMessageConverter() {
                        @Override
                        public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                            Object o = super.read(type, contextClass, inputMessage);
                            String oStr = JacksonUtil.bean2Json(o);
                            MyModelAndViewResolver.params.get().setP2(oStr);//set到 自定义的Params中去
                            return o;
                        }
                    };
            bean2.setObjectMapper(((MappingJackson2HttpMessageConverter) bean).getObjectMapper());
            bean = bean2;

        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 封装一下参数对象，因为当json的时候是获取不到get，post的一些servlet参数的
     */
    public static class Param {
        Object p1;
        Object p2;

        public Param() {
        }

        public void clear() {
            p1 = null;
            p2 = null;
        }

        public Object getP1() {
            return p1;
        }

        public void setP1(Object p1) {
            this.p1 = p1;
        }

        public Object getP2() {
            return p2;
        }

        public void setP2(Object p2) {
            this.p2 = p2;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "p1=" + p1 +
                    ", p2=" + p2 +
                    '}';
        }
    }

    public class MyRequestResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

        public MyRequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
            super(messageConverters);
        }

        @Override
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
            handlerMonitor(returnType.getMethod(), returnValue, (ServletWebRequest) webRequest);
            super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
        }
    }
}
