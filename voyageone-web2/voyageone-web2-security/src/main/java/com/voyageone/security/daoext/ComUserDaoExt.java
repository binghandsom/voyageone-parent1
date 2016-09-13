
package com.voyageone.security.daoext;

import com.voyageone.security.bean.ComChannelPermissionBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComUserDaoExt {
    List<ComChannelPermissionBean> selectPermissionChannel(Integer userId);


    List<String> getPermissionUrls(@Param("userId") Integer userId, @Param("channelId") String channelId, @Param("application") String application);
}