package com.voyageone.web2.core.views;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.CoreConstants;
import com.voyageone.web2.core.dao.UserConfigDao;
import com.voyageone.web2.core.dao.UserDao;
import com.voyageone.web2.core.model.ChannelPermissionBean;
import com.voyageone.web2.core.model.UserBean;
import com.voyageone.web2.core.model.UserConfigBean;
import com.voyageone.web2.core.model.UserSessionBean;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserService extends BaseAppService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserConfigDao userConfigDao;

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
        userSessionBean.setUserConfig(getUserConfig(userBean.getId()));

        return userSessionBean;
    }

    public List<ChannelPermissionBean> getPermissionCompany(UserSessionBean userSessionBean) {
        return userDao.selectPermissionChannel(userSessionBean.getUserName());
    }

    private Map<String , List<UserConfigBean>> getUserConfig(int userId) {
        // 查询 ct_user_config 获取所有该用户的配置信息
        // 如果一个属性有多件的话，就放入List
        // 也就是说 HASHMAP里的每一个属性，是一个集合，这个集合可能是一件，也可能是多件
        // 时区和所属仓库是常用属性，所以独立出来，但是这里也会取到
        List<UserConfigBean> ret = userConfigDao.select(userId);
        return ret.stream().collect(groupingBy(UserConfigBean::getCfg_name, toList()));
    }
}
