package com.voyageone.web2.core.views.user;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.security.bean.ComChannelPermissionBean;
import com.voyageone.security.dao.ComUserConfigDao;
import com.voyageone.security.dao.ViewUserResDao;
import com.voyageone.security.daoext.ComUserDaoExt;
import com.voyageone.security.model.ComUserConfigModel;
import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.bean.com.PermissionBean;
import com.voyageone.service.bean.com.UserBean;
import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.CoreConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.service.daoext.com.UserConfigDao;
import com.voyageone.service.daoext.com.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
public class UserService extends BaseAppService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ComUserConfigDao comUserConfigDao;


    @Autowired
    private ComUserDaoExt comUserDaoExt;

//    @Autowired
//    private UserConfigDao userConfigDao;




    public UserSessionBean login(String username, String password, int timezone) {

        UserBean userBean = new UserBean();
        userBean.setUsername(username);

        userBean = userDao.selectUser(userBean);

        if (userBean == null)
            throw new BusinessException("没有用户");

        String cryptoPassword = new Md5Hash(password, username.toLowerCase() + CoreConstants.MD5_FIX_SALT,
                CoreConstants.MD5_HASHITERATIONS).toHex();

        if (!userBean.getPassword().equals(cryptoPassword))
            throw new BusinessException("密码错误");

        // 填充用户信息到 Session. 权限部分需要在选择了渠道后获取
        UserSessionBean userSessionBean = new UserSessionBean();
        userSessionBean.setUserId(userBean.getId());
        userSessionBean.setUserName(userBean.getUsername());
        userSessionBean.setTimeZone(timezone);
//        userSessionBean.setUserConfig(getUserConfig(userBean.getId()));

        return userSessionBean;
    }

    public List<ChannelPermissionBean> getPermissionCompany(UserSessionBean userSessionBean) {


        List<ComChannelPermissionBean>  list =  comUserDaoExt.selectPermissionChannel(userSessionBean.getUserId());

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

//        return userDao.selectPermissionChannel(userSessionBean.getUserName());



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

        List<String> permissionUrls = getPermissionUrls(user, channelId);

        // 设置当前用户选择的公司
        user.setSelChannel(channel);
        user.setActionPermission(permissionUrls);
        user.setApplicationId(applicationId);
        user.setApplication(application);
        // 转换为页面的权限地址
        List<String> pagePermissions = permissionUrls.stream()
                .map(url -> url.substring(0, url.lastIndexOf("/")))
                .distinct()
                .collect(toList());

        user.setPagePermission(pagePermissions);
    }

    public String getUserLanguage (UserSessionBean user) {
        List<UserConfigBean> languageInfo = user.getUserConfig().get(CoreConstants.USER_CONFIG_LANGUAGE_ID);

        return languageInfo != null && languageInfo.size() > 0 ? languageInfo.get(0).getCfg_val1() : "cn";
    }

    public Map<String , List<UserConfigBean>> getUserConfig(int userId) {
//        List<UserConfigBean> ret = userConfigDao.select(userId);
//        return ret.stream().collect(groupingBy(UserConfigBean::getCfg_name, toList()));

        Map map = new HashMap<>();
        map.put("userId", userId);
        map.put("active", true);
        List<ComUserConfigModel>  list =  comUserConfigDao.selectList(map);
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

    private List<String> getPermissionUrls(UserSessionBean userSessionBean, String channelId) {

        List<PermissionBean> rolePermissions = userDao.selectRolePermissions(channelId, userSessionBean.getUserName());

        List<PermissionBean> userPermissions = userDao.selectUserPermissions(channelId, userSessionBean.getUserName());

        return Stream.concat(rolePermissions.stream(), userPermissions.stream())
                .filter(PermissionBean::isEnabled)
                .map(permissionBean -> String.format("/%s/%s/%s/%s",
                        permissionBean.getApplication(),
                        permissionBean.getModule(),
                        permissionBean.getController(),
                        permissionBean.getAction()))
                .distinct()
                .collect(toList());
    }
}
