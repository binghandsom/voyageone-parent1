package com.voyageone.web2.core.views.user;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.security.bean.ComChannelPermissionBean;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
@RequestMapping(value = CoreUrlConstants.USER.ROOT, method = RequestMethod.POST)
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private ComUserService comUserService;

    @RequestMapping(CoreUrlConstants.USER.LOGIN)
    public AjaxResponse login(@RequestBody Map<String, Object> params) {

        String username = (String) params.get("username");
        String password = (String) params.get("password");
        int timezone = (int) params.get("timezone");

        String app = (String) params.getOrDefault("application", "cms");

//        // 验证在内部
//        // 登录成功返回, 否则通过 BusinessException 返回
//        UserSessionBean userSessionBean = userService.login(username, password, timezone);
//        // 保存用户
//        getSession().setAttribute(BaseConstants.SESSION_USER, userSessionBean);
//        // 保存用户的默认语言
//        getSession().setAttribute(BaseConstants.SESSION_LANG, userService.getUserLanguage(userSessionBean));


        ComUserModel userModel = comUserService.login(username, password, app);

        Session session = SecurityUtils.getSubject().getSession();
//        ComUserModel userModel = (ComUserModel)session.getAttribute("comUserModel");
        // 填充用户信息到 Session. 权限部分需要在选择了渠道后获取
        UserSessionBean userSessionBean = new UserSessionBean();
        userSessionBean.setUserId(userModel.getId());
        userSessionBean.setUserName(userModel.getUserAccount());
        userSessionBean.setTimeZone(timezone);
        userSessionBean.setUserConfig(userService.getUserConfig(userModel.getId()));
        session.setAttribute(BaseConstants.SESSION_USER, userSessionBean);
        session.setAttribute(BaseConstants.SESSION_LANG, userService.getUserLanguage(userSessionBean));

        session.setAttribute("comUserModel", userModel);
        session.setAttribute("userId", userModel.getId());

        // 返回用户信息
        return success(true);
    }

    @RequestMapping(CoreUrlConstants.USER.VENDOR_LOGIN)
    public AjaxResponse vendorLogin(@RequestBody Map<String, Object> params) {

        String username = (String) params.get("username");
        String password = (String) params.get("password");
        int timezone = (int) params.get("timezone");

//        // 验证在内部
//        // 登录成功返回, 否则通过 BusinessException 返回
//        UserSessionBean userSessionBean = userService.login(username, password, timezone);
//        // 保存用户
//        getSession().setAttribute(BaseConstants.SESSION_USER, userSessionBean);
//        // 保存用户的默认语言
//        getSession().setAttribute(BaseConstants.SESSION_LANG, userService.getVendorUserLanguage(userSessionBean));
//
//        // 取得user对应的channelId
//        List<UserConfigBean> userConfigBeanList = userSessionBean.getUserConfig().get("channel_id");
//
//        // 设置channel_id
//        if (userConfigBeanList != null && userConfigBeanList.size() > 0) {
//            userService.setSelectChannel(userSessionBean, userConfigBeanList.get(0).getCfg_val1(), "99", "vms");
//        } else {
//            throw new BusinessException("Invalid  User.");
//        }


        comUserService.login(username, password, "vms");

        Session session = SecurityUtils.getSubject().getSession();
        ComUserModel userModel = (ComUserModel)session.getAttribute("comUserModel");
        // 填充用户信息到 Session. 权限部分需要在选择了渠道后获取
        UserSessionBean userSessionBean = new UserSessionBean();
        userSessionBean.setUserId(userModel.getId());
        userSessionBean.setUserName(userModel.getUserAccount());
        userSessionBean.setTimeZone(timezone);
        userSessionBean.setUserConfig(userService.getUserConfig(userModel.getId()));

        session.setAttribute(BaseConstants.SESSION_USER, userSessionBean);

//         保存用户的默认语言
        getSession().setAttribute(BaseConstants.SESSION_LANG, userService.getVendorUserLanguage(userSessionBean));

        // 取得user对应的channelId
        List<UserConfigBean> userConfigBeanList = userSessionBean.getUserConfig().get("channel_id");

        // 设置channel_id
        if (userConfigBeanList != null && userConfigBeanList.size() > 0) {
            userService.setSelectChannel(userSessionBean, userConfigBeanList.get(0).getCfg_val1(), "99", "vms");
        } else {
            throw new BusinessException("Invalid  User.");
        }


        // 返回用户信息
        return success(true);
    }

    @RequestMapping(CoreUrlConstants.USER.GET_CHANNEL)
    public AjaxResponse getChannel() {
        List<ChannelPermissionBean> companyBeans = userService.getPermissionCompany(getUser());
        return success(companyBeans);
    }

    @RequestMapping(CoreUrlConstants.USER.SELECT_CHANNEL)
    public AjaxResponse selectChannel(@RequestBody Map<String, Object> params) {

        getSession().setAttribute("voyageone.session.cms", null);
        getSession().setAttribute("channelId", params.get("channelId").toString());
        getSession().setAttribute("applicationId", params.get("applicationId").toString());
        getSession().setAttribute("application", params.get("application").toString());

        userService.setSelectChannel(getUser(),params.get("channelId").toString(),params.get("applicationId").toString(),params.get("application").toString());
        // 只要不报异常就是ok
        return success(true);
    }

    /**
     * logout处理
     */
    @RequestMapping(CoreUrlConstants.USER.LOGOUT)
    public AjaxResponse logout() {

        // 清空缓存
        HttpSession session = getSession();
        if (session != null) {
            session.invalidate();
        }
        SecurityUtils.getSubject().logout();

        // 只要不报异常就是ok
        return success(true);
    }
}
