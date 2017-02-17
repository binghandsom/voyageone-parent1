
package com.voyageone.service.daoext.user;


import com.voyageone.service.bean.user.ComChannelPermissionBean;
import com.voyageone.service.model.user.ComRoleModel;
import com.voyageone.service.model.user.ViewResUserCompanyModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ComUserDaoExt {
    List<ComChannelPermissionBean> selectPermissionChannel(Integer userId);


    List<String> getPermissionUrls(@Param("userId") Integer userId, @Param("channelId") String channelId, @Param("application") String application);


    List<ComRoleModel> selectRolesByUserId(@Param("userId") Integer userId, @Param("channelId") String channelId);

    List<String> selectAppsByUser(Integer userId);

    List<ViewResUserCompanyModel> selectAction(Object map);


    List<String> selectChannels(Object map);


    List<Map<String, String>> selectChannelsByUser (String userAccount);
}