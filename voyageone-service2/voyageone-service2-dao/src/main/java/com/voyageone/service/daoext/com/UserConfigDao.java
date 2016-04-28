package com.voyageone.service.daoext.com;

import com.voyageone.service.bean.com.UserConfigBean;
import com.voyageone.service.dao.ServiceBaseDao;
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
public class UserConfigDao extends ServiceBaseDao {

    public List<UserConfigBean> select(int userId) {
        return selectList("ct_user_config_select", userId);
    }
}
