package com.voyageone.web2.core.views.user;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.service.bean.user.ComChannelPermissionBean;
import com.voyageone.service.model.user.ComUserConfigModel;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.CoreConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Index 路径
 * Created on 11/26/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Service
public class UserService extends BaseViewService {

    @Autowired
    private ComUserService comUserService;

    public List<ChannelPermissionBean> getPermissionCompany(UserSessionBean userSessionBean) {

//        return userDao.selectPermissionChannel(userSessionBean.getUserName());

        List<ComChannelPermissionBean>  list =  comUserService.getPermissionCompany(userSessionBean.getUserId());

        List<ChannelPermissionBean>  ret = new ArrayList<>();

        for(ComChannelPermissionBean  model : list)
        {
            ChannelPermissionBean  bean = new ChannelPermissionBean();
            bean.setApps(model.getApps());
            bean.setChannelId(model.getChannelId());
            bean.setChannelImgUrl(model.getChannelImgUrl());
            bean.setCompanyId(model.getCompanyId());
            bean.setChannelName(model.getChannelName());
            bean.setCompanyName(model.getCompanyName());
            bean.setApps(model.getApps());
            ret.add(bean);
        }

        return ret;
    }

    public void setSelectChannel(UserSessionBean user, String channelId,String applicationId,String application) {

        if (StringUtils.isEmpty(channelId))
            throw new BusinessException("");

        Channel channel = Channel.valueOfId(channelId);

        if (channel == null)
            throw new BusinessException("");

        // 在设置之前,检查一下是否需要重新查询,因为画面虽然选择的 app,但是查询的数据是 channel 级别
        if (channel.equals(user.getSelChannel())) {
            return;
        }

//        List<String> permissionUrls = getPermissionUrls(user, channelId);

        // 设置当前用户选择的公司
        user.setSelChannel(channel);
//        user.setActionPermission(permissionUrls);
        user.setApplicationId(applicationId);
        user.setApplication(application);
        // 转换为页面的权限地址
//        List<String> pagePermissions = permissionUrls.stream()
//                .map(url -> url.substring(0, url.lastIndexOf("/")))
//                .distinct()
//                .collect(toList());

//        user.setPagePermission(pagePermissions);

        //不需要再查询授权信息了
        user.setActionPermission(new ArrayList<>());
        user.setPagePermission(new ArrayList<>());

        //清除该用户的授权缓存
        comUserService.clearCachedAuthorizationInfo();

    }

    public String getUserLanguage (UserSessionBean user) {
        List<UserConfigBean> languageInfo = user.getUserConfig().get(CoreConstants.USER_CONFIG_LANGUAGE_ID);

        return languageInfo != null && languageInfo.size() > 0 ? languageInfo.get(0).getCfg_val1() : "cn";
    }

    public Map<String , List<UserConfigBean>> getUserConfig(int userId) {
//        List<UserConfigBean> ret = userConfigDao.select(userId);
//        return ret.stream().collect(groupingBy(UserConfigBean::getCfg_name, toList()));

        List<ComUserConfigModel>  list = comUserService.getUserConfig(userId);
        List<UserConfigBean> ret = new ArrayList<>();

        for(ComUserConfigModel model : list)
        {
            UserConfigBean bean = new UserConfigBean();
            bean.setCfg_name(model.getCfgName());
            bean.setCfg_val1(model.getCfgVal1());
            bean.setCfg_val2(model.getCfgVal2());
            bean.setComment(model.getComment());
            bean.setUser_id(model.getUserId());
            ret.add(bean);
        }
        return ret.stream().collect(groupingBy(UserConfigBean::getCfg_name, toList()));
    }

    public String getVendorUserLanguage (UserSessionBean user) {
        List<UserConfigBean> languageInfo = user.getUserConfig().get(CoreConstants.USER_CONFIG_LANGUAGE_ID);

        return languageInfo != null && languageInfo.size() > 0 ? languageInfo.get(0).getCfg_val1() : "en";
    }



    @Deprecated
    private List<String> getPermissionUrls(UserSessionBean userSessionBean, String channelId) {
        return  comUserService.getPermissionUrls(userSessionBean.getUserId(), channelId, "cms");
    }

}
