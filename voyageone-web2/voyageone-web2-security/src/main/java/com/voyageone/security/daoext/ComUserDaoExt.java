
package com.voyageone.security.daoext;

import com.voyageone.security.bean.ComChannelPermissionBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComUserDaoExt {
    List<ComChannelPermissionBean> selectPermissionChannel(Integer userId);
}