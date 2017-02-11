package com.voyageone.security.shiro.filter;

import com.voyageone.common.util.JacksonUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-30.
 */
public class MyAuthenticationFilter extends FormAuthenticationFilter {


    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        String loginUrl = this.getLoginUrl();
//        WebUtils.issueRedirect(request, response, loginUrl);

        response.setCharacterEncoding("UTF-8");
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("code", "A001");
        resultMap.put("message", "user not login!");
        resultMap.put("redirectTo", loginUrl);

        PrintWriter out = response.getWriter();
        out.println(JacksonUtil.bean2Json(resultMap));
    }
}
