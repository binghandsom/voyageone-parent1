package com.voyageone.web2.base.interceptors;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * 对权限进行检测
 *
 * @author Jonas
 * @version 2.0.0, 11/27/15
 */
@Component
class AuthorizationInterceptor {

    private final static Set<String> excludes = new HashSet<String>() {{
        add("/core/access/user/getChannel");
        add("/core/access/user/selectChannel");
    }};

    public boolean preHandle(HttpServletRequest request) throws Exception {

        if (skip(request)) return true;

        HttpSession session = request.getSession();
        UserSessionBean user = (UserSessionBean) session.getAttribute(BaseConstants.SESSION_USER);

        if (user.getSelChannel() == null)
            throw new SystemException(BaseConstants.CODE_SEL_CHANNEL, Constants.EmptyString);

        String url = request.getRequestURI();
        String project = request.getContextPath();

        // 如果没有权限信息的话,通过 CODE 通知 Handler 和前台,跳转到渠道的选择页面去.
        if (user.getActionPermission() == null)
            throw new SystemException(BaseConstants.CODE_SEL_CHANNEL, Constants.EmptyString);

        // 检查用户是否有权限, 没有权限则不再继续
        boolean isAuth = user.getActionPermission().contains(url.replaceFirst(project, ""));

        if (!isAuth)
            throw new BusinessException(BaseConstants.MSG_DENIED);

        return true;
    }

    private boolean skip(HttpServletRequest request) {

        //return excludes.contains(request.getRequestURI());
        // TODO 开发阶段跳过全部检查
        return true;
    }
}
