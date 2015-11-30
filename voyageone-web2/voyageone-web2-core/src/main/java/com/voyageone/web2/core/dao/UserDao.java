package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.core.model.ChannelPermissionBean;
import com.voyageone.web2.core.model.UserBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
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

    public UserBean selectUser(UserBean userBean) {
        return selectOne("ct_user_selectUser", userBean);
    }

    public List<ChannelPermissionBean> selectPermissionChannel(String userName) {
        return selectList("ct_user_selectPermissionChannel", userName);
    }
}
