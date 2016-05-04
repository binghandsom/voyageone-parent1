package com.voyageone.service.daoext.com;

import com.voyageone.service.bean.com.ChannelPermissionBean;
import com.voyageone.service.bean.com.PermissionBean;
import com.voyageone.service.bean.com.UserBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据处理
 * Created on 11/27/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Repository
public class UserDao extends ServiceBaseDao {

    public UserBean selectUser(UserBean userBean) {
        return selectOne("ct_user_selectUser", userBean);
    }

    public List<ChannelPermissionBean> selectPermissionChannel(String userName) {
        return selectList("ct_user_selectPermissionChannel", userName);
    }

    public List<PermissionBean> getRolePermissions(String channelId, String userName) {
        return selectList("ct_role_permission_getPermissionByRole", parameters("channelId", channelId, "userName", userName));
    }

    public List<PermissionBean> getUserPermissions(String channelId, String userName) {
        return selectList("ct_user_permission_getPermissionByUser", parameters("channelId", channelId, "userName", userName));
    }
}
