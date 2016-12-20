package com.voyageone.security.shiro.filter;

import com.voyageone.common.util.JacksonUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-12-02.
 */
public class ChannelCheckFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = this.getSubject(servletRequest, servletResponse);

        if (subject.getPrincipal() != null) {
            Session session = subject.getSession();
            String requestURI = getPathWithinApplication(servletRequest);
            if (requestURI.startsWith("/cms")) {
                Object objChannelId = session.getAttribute("channelId");
                if (objChannelId != null) {
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            saveRequest(request);
            //跳转到登录页面,返回json消息
            response.setCharacterEncoding("UTF-8");
            Map<String, String> resultMap = new HashMap<>();

            resultMap.put("code", "A001");
            resultMap.put("message", "user not login!");

            PrintWriter out = response.getWriter();
            out.println(JacksonUtil.bean2Json(resultMap));

        } else {
            //返回json消息
            Map<String, String> resultMap = new HashMap<String, String>();

            //返回json消息
            response.setCharacterEncoding("UTF-8");

            resultMap.put("code", "SYS_0");
            resultMap.put("message", "invalid channelId");
            resultMap.put("redirectTo", "/");

            PrintWriter out = response.getWriter();
            out.println(JacksonUtil.bean2Json(resultMap));
        }

        return false;
    }
}
