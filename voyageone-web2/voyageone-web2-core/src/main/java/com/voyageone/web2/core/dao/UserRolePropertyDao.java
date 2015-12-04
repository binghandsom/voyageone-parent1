package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ct_user_role_property
 *
 * @author         Edward
 * @version        2.0.0, 15/12/01
 */
@Repository
public class UserRolePropertyDao extends WebBaseDao {

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    /**
     * 根据userId和channelId获取用户的menu列表.
     * @param data
     * @return
     */
    public List<Map<String, Object>> selectUserMenu(Map<String, Object> data) {
        return selectList("ct_user_role_property_selectUserMenu", data);
    }
}
