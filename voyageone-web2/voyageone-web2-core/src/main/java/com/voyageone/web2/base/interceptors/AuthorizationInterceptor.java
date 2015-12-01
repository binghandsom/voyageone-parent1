package com.voyageone.web2.base.interceptors;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants.LANGUAGE;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.message.MessageService;
import com.voyageone.web2.core.bean.UserSessionBean;
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
        UserSessionBean user = (UserSessionBean) session.getAttribute(BaseConstants.SESSION_USER);

        if (user == null) {
            AjaxResponse ajaxResponse = new AjaxResponse();
            ajaxResponse.setCode(BaseConstants.MSG_TIMEOUT);
            ajaxResponse.setMessage(messageService.getMessage(LANGUAGE.EN, BaseConstants.MSG_TIMEOUT));
            ajaxResponse.writeTo(response);
            return false;
        }

        String url = request.getRequestURI();
        String project = request.getContextPath();

        if (user.getActionPermission() == null)
            throw new BusinessException(BaseConstants.MSG_DENIED);

        // 检查用户是否有权限, 没有权限则不再继续
        boolean isAuth = user.getActionPermission().contains(url.replaceFirst(project, ""));

        if (!isAuth)
            throw new BusinessException(BaseConstants.MSG_DENIED);

        return true;
    }
}
