package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.core.model.ChannelPermissionModel;
import com.voyageone.web2.core.model.PermissionModel;
import com.voyageone.web2.core.model.UserModel;
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
public class UserDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    public UserModel selectUser(UserModel userBean) {
        return selectOne("ct_user_selectUser", userBean);
    }

    public List<ChannelPermissionModel> selectPermissionChannel(String userName) {
        return selectList("ct_user_selectPermissionChannel", userName);
    }

    public List<PermissionModel> getRolePermissions(String channelId, String userName) {
        return selectList("ct_role_permission_getPermissionByRole", parameters("channelId", channelId, "userName", userName));
    }

    public List<PermissionModel> getUserPermissions(String channelId, String userName) {
        return selectList("ct_user_permission_getPermissionByUser", parameters("channelId", channelId, "userName", userName));
    }
}
