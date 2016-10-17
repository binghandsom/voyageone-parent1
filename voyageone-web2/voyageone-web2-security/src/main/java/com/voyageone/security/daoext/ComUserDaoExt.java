
package com.voyageone.security.daoext;

import com.voyageone.security.bean.ComChannelPermissionBean;
import com.voyageone.security.model.ComRoleModel;
import com.voyageone.security.model.ViewResUserCompanyModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComUserDaoExt {
    List<ComChannelPermissionBean> selectPermissionChannel(Integer userId);


    List<String> getPermissionUrls(@Param("userId") Integer userId, @Param("channelId") String channelId, @Param("application") String application);


    List<ComRoleModel> selectRolesByUserId(Integer userId);

    List<String> selectAppsByUser(Integer userId);

    List<ViewResUserCompanyModel> selectAction(Object map);
}