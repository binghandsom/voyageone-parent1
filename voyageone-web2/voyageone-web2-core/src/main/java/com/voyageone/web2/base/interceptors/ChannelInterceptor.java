package com.voyageone.web2.base.interceptors;

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
 * 检查用户是否选择了渠道
 *
 * @author Jonas, 1/19/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
class ChannelInterceptor {

    private final static Set<String> excludes = new HashSet<String>() {{
        add("/core/access/user/getChannel");
        add("/core/access/user/selectChannel");
        add("/core/access/user/logout");
    }};

    boolean preHandle(HttpServletRequest request) throws Exception {

        if (skip(request)) return true;

        HttpSession session = request.getSession();
        UserSessionBean user = (UserSessionBean) session.getAttribute(BaseConstants.SESSION_USER);

        if (user.getSelChannel() == null)
            throw new SystemException(BaseConstants.CODE_SEL_CHANNEL, Constants.EmptyString);

        return true;
    }

    private boolean skip(HttpServletRequest request) {
        return excludes.contains(request.getRequestURI());
    }
}
