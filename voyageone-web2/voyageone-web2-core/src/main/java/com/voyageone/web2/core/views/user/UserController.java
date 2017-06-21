package com.voyageone.web2.core.views.user;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.model.user.ComUserModel;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.base.BaseConstants;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.core.CoreUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


        ComUserModel userModel = comUserService.login(username, password, "vms");

        //取得user对应的channelId
        List<String> channels = comUserService.selectChannels(userModel.getId());
        if(channels == null || channels.size() == 0)
        {
            throw new BusinessException("Invalid  User.");
        }

        Session session = SecurityUtils.getSubject().getSession();
        // 填充用户信息到 Session. 权限部分需要在选择了渠道后获取
        UserSessionBean userSessionBean = new UserSessionBean();
        userSessionBean.setUserId(userModel.getId());
        userSessionBean.setUserName(userModel.getUserAccount());
        userSessionBean.setTimeZone(timezone);
        Map<String, List<UserConfigBean>>  config = userService.getUserConfig(userModel.getId());

        List<UserConfigBean> cfgList =  new ArrayList<>();
        for (String channel: channels) {
            UserConfigBean cfg = new UserConfigBean();
            cfg.setCfg_name("channel_id");
            cfg.setCfg_val1(channel);
            cfg.setCfg_val2("");
            cfg.setComment("");
            cfg.setUser_id(userModel.getId());
            cfgList.add(cfg);
        }
        config.put("channel_id", cfgList);

        userSessionBean.setUserConfig(config);



        userService.setSelectChannel(userSessionBean, channels.get(0), "99", "vms");

        session.setAttribute(BaseConstants.SESSION_USER, userSessionBean);

//         保存用户的默认语言
        getSession().setAttribute(BaseConstants.SESSION_LANG, userService.getVendorUserLanguage(userSessionBean));

        // 取得user对应的channelId
//        List<UserConfigBean> userConfigBeanList = userSessionBean.getUserConfig().get("channel_id");
//
//        // 设置channel_id
//        if (userConfigBeanList != null && userConfigBeanList.size() > 0) {
//            userService.setSelectChannel(userSessionBean, userConfigBeanList.get(0).getCfg_val1(), "99", "vms");
//        } else {
//            throw new BusinessException("Invalid  User.");
//        }




        session.setAttribute("comUserModel", userModel);
        session.setAttribute("userId", userModel.getId());


        // 返回用户信息
        return success(true);
    }

    @RequestMapping(CoreUrlConstants.USER.GET_CHANNEL)
    public AjaxResponse getChannel() {

        List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeChannelBeansByTypeLang(Constants.comMtTypeChannel.SKU_CARTS_53, "cn");
        List<String> channelIds = typeChannelBeans
                .stream()
                .filter(item->!"1".equals(item.getValue()) && !"0".equals(item.getValue()) && !item.getValue().startsWith("928") && !"22".equals(item.getValue()))
                .map(TypeChannelBean::getChannel_id)
                .distinct()
                .collect(Collectors.toList());

        List<ChannelPermissionBean> companyBeans = userService.getPermissionCompany(getUser());
        if(ListUtils.notNull(companyBeans)){
            companyBeans = companyBeans.stream().filter(item->channelIds.contains(item.getChannelId())).collect(Collectors.toList());
            companyBeans.sort((o1, o2) -> o1.getChannelId().compareTo(o2.getChannelId()));
        }
        return success(companyBeans);
    }

    @RequestMapping(CoreUrlConstants.USER.SELECT_CHANNEL)
    public AjaxResponse selectChannel(@RequestBody Map<String, Object> params) {

        getSession().setAttribute("voyageone.session.cms", null);

        userService.setSelectChannel(getUser(),params.get("channelId").toString(),params.get("applicationId").toString(),params.get("application").toString());
        getSession().setAttribute("channelId", params.get("channelId").toString());
        getSession().setAttribute("applicationId", params.get("applicationId").toString());
        getSession().setAttribute("application", params.get("application").toString());

        //清除用户授权缓存
        try {
            comUserService.clearCachedAuthorizationInfo();
        }
        catch (Exception e)
        {
            //do nothing
        }


        // 只要不报异常就是ok
        return success(true);
    }

    /**
     * logout处理
     */
    @RequestMapping(CoreUrlConstants.USER.LOGOUT)
    public AjaxResponse logout() {

        comUserService.logout();

        // 只要不报异常就是ok
        return success(true);
    }
}
