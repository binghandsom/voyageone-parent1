package com.voyageone.service.daoext.core;

import com.voyageone.service.bean.com.AdminUserBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@Repository
public interface AdminUserDaoExt {

    List<AdminUserBean> selectUserByPage(Map<String, Object> params);

    Integer selectUserCount(Map<String, Object> params);


}
