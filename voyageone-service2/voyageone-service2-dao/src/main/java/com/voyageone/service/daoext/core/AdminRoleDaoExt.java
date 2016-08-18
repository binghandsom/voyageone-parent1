package com.voyageone.service.daoext.core;

import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.bean.com.AdminUserBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@Repository
public interface AdminRoleDaoExt {

    List<AdminRoleBean> selectRoleByPage(Map<String, Object> params);

    Integer selectRoleCount(Map<String, Object> params);


}
