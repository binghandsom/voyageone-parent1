package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.core.model.UserConfigModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ct_user_config 表
 * Created on 11/28/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
@Repository
public class UserConfigDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    public List<UserConfigModel> select(int userId) {
        return selectList("ct_user_config_select", userId);
    }
}
