package com.voyageone.web2.core.views;

import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.core.model.ChannelPermissionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Index 路径
 *
 * @author Jonas, 11/26/15.
 * @version 2.0.0
 */
@RestController
@RequestMapping(value = CoreUrlConstants.USER_ROOT, method = RequestMethod.POST)
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(CoreUrlConstants.USER_LOGIN)
    public AjaxResponse login(@RequestBody Map<String, Object> params) {

        String username = (String) params.get("username");
        String password = (String) params.get("password");
        int timezone = (int) params.get("timezone");

        // 验证在内部
        // 登录成功返回, 否则通过 BusinessException 返回
        UserSessionBean userSessionBean = userService.login(username, password, timezone);
        // 保存用户
        getSession().setAttribute(BaseConstants.SESSION_USER, userSessionBean);

        // 返回用户信息
        return success(true);
    }

    @RequestMapping(CoreUrlConstants.USER_CHANNEL)
    public AjaxResponse getChannel() {
        List<ChannelPermissionModel> companyBeans = userService.getPermissionCompany(getUser());
        return success(companyBeans);
    }

    @RequestMapping(CoreUrlConstants.USER_SEL_CHANNEL)
    public AjaxResponse selectChannel(@RequestBody Map<String, Object> params) {
        userService.setSelectChannel(getUser(), String.valueOf(params.get("channelId")));
        // 只要不报异常就是ok
        return success(true);
    }

    /**
     * logout处理
     */
    @RequestMapping(CoreUrlConstants.USER_LOGOUT)
    public AjaxResponse logout() {

        // 清空缓存
        HttpSession session = getSession();
        if (session != null) {
            session.invalidate();
        }

        // 只要不报异常就是ok
        return success(true);
    }
}
