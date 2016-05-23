package com.voyageone.web2.base.interceptors;

import com.voyageone.base.exception.SystemException;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 对登陆进行检查
 * @author Jonas
 * @version 2.0.0, 2015-12-02 15:42:18
 */
@Component
class LoginInterceptor {

    boolean preHandle(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        UserSessionBean user = (UserSessionBean) session.getAttribute(BaseConstants.SESSION_USER);

        if (user == null)
            throw new SystemException(BaseConstants.MSG_TIMEOUT, "未登录用户或登录超时");

        return true;
    }
}
