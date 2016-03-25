package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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
     * 更具用户Id和渠道Id取得该用户的所对应的权限列表(TODO: 只是临时用户cms2的翻译权限使用)
     * @param data
     * @return
     */
    public int selectUserRoleProperties(Map<String, Object> data) {
        return selectOne("ct_user_role_property_selectUserRoleByIdAndChannelId", data);
    }

    /**
     * 根据userId和channelId获取用户的application列表.
     *
     * @param data
     * @return
     */
    public List<Map<String, Object>> selectUserApplication(Map<String, Object> data) {
        return selectList("ct_user_role_property_selectUserApplication", data);
    }

    /**
     * 根据userId propertyId  applicationId获取用户权限的菜单列表.
     *
     * @param userId
     * @param propertyId
     * @param applicationId
     * @return
     */
    public List<Map<String, Object>> getListModuleByWhere(String userId,String propertyId,String  applicationId) {
        //userId propertyId  applicationId
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("propertyId", propertyId);
        data.put("applicationId", applicationId);
        return selectList("ct_user_role_property_getListModuleByWhere", data);
    }

    /**
     * 根据userId propertyId  applicationId获取角色权限的菜单列表.
     *
     * @param userId
     * @param propertyId
     * @param applicationId
     * @return
     */
    public List<Map<String, Object>> getListControllerByWhere(String userId,String propertyId,String  applicationId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("propertyId", propertyId);
        data.put("applicationId", applicationId);
        return selectList("ct_user_role_property_getListControllerByWhere", data);
    }
}
