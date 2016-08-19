package com.voyageone.web2.base.interceptors;

import com.voyageone.common.logger.VOAbsLoggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截管理器, 统一处理所有过滤操作
 * Created on 11/27/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public class Interceptor extends VOAbsLoggable implements HandlerInterceptor {

    private final AuthorizationInterceptor authorizationInterceptor;

    private final LoginInterceptor loginInterceptor;

    private final ChannelInterceptor channelInterceptor;

    @Autowired
    public Interceptor(AuthorizationInterceptor authorizationInterceptor, LoginInterceptor loginInterceptor, ChannelInterceptor channelInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.loginInterceptor = loginInterceptor;
        this.channelInterceptor = channelInterceptor;
    }

    /**
     * Intercept the execution of a handler. Called after HandlerMapping determined
     * an appropriate handler object, but before HandlerAdapter invokes the handler.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending a HTTP error or writing a custom response.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        $info(request.getServletPath() + " is start.");

//        $debug(this.getRequestBody(request));

        // vms系统没有channel选择画面所以channelInterceptor不需要
        if (request.getServletPath().startsWith("/vms")) {
            return loginInterceptor.preHandle(request)
                    && authorizationInterceptor.preHandle(request);
        }

        return loginInterceptor.preHandle(request)
            && channelInterceptor.preHandle(request)
            && authorizationInterceptor.preHandle(request);
    }

    /**
     * Intercept the execution of a handler. Called after HandlerAdapter actually
     * invoked the handler, but before the DispatcherServlet renders the view.
     * Can expose additional model objects to the view via the given ModelAndView.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can post-process an execution,
     * getting applied in inverse order of the execution chain.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        $info(request.getServletPath() + " is end.");
    }

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     * <p>As with the {@code postHandle} method, the method will be invoked on each
     * interceptor in the chain in reverse order, so the first interceptor will be
     * the last to be invoked.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 暂时 Do nothing
    }

    private String getRequestBody(HttpServletRequest request) {
        if (null != request && request.getContentLength() > 0) {
            byte[] requestBody = new byte[request.getContentLength()];
            try {
                request.getInputStream().read(requestBody, 0, request.getContentLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(requestBody);
        }
        return "";
    }
}
