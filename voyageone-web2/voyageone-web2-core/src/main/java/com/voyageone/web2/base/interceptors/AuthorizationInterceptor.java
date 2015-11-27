package com.voyageone.web2.base.interceptors;

import com.voyageone.common.Constants.LANGUAGE;
import com.voyageone.web2.base.Constants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.message.MessageService;
import com.voyageone.web2.core.model.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 授权检查
 * Created on 11/27/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Component
class AuthorizationInterceptor {
    @Autowired
    private MessageService messageService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        UserSessionBean user = (UserSessionBean) session.getAttribute(Constants.SESSION_USER);

        if (user == null) {
            AjaxResponse ajaxResponse = new AjaxResponse();
            ajaxResponse.setCode(Constants.MSG_TIMEOUT);
            ajaxResponse.setMessage(messageService.getMessage(LANGUAGE.EN, Constants.MSG_TIMEOUT));
            ajaxResponse.writeTo(response);
            return false;
        }

        String url = request.getRequestURI();
        String project = request.getContextPath();

        // 检查用户是否有权限, 没有权限则不再继续
        boolean isAuth = user.getActionPermission().contains(url.replaceFirst(project, ""));

        if (isAuth) return true;

        Object val = session.getAttribute(Constants.SESSION_LANG);

        String lang = val == null ||
                !val.equals(LANGUAGE.EN) ||
                !val.equals(LANGUAGE.CN) ||
                !val.equals(LANGUAGE.JP)
                    ? LANGUAGE.EN
                    : val.toString();

        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setCode(Constants.MSG_DENIED);
        ajaxResponse.setMessage(messageService.getMessage(lang, Constants.MSG_DENIED));
        ajaxResponse.writeTo(response);

        return false;
    }
}
