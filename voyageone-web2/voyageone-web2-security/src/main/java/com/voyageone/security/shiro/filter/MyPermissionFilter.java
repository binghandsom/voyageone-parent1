package com.voyageone.security.shiro.filter;

import com.voyageone.common.util.JacksonUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-29.
 */
public class MyPermissionFilter extends PermissionsAuthorizationFilter {



    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

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
            Map<String, String> result = new HashMap<>();

            resultMap.put("code", "A002");
            resultMap.put("message", "user unauthorized!");

            PrintWriter out = response.getWriter();
            out.println(JacksonUtil.bean2Json(result));
        }

        return false;
    }
}
