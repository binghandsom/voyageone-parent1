package com.voyageone.service.daoext.cms;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtPlatformNumiidDaoExt {

    int updateStatusByNumiids(@Param("channelId") String channelId, @Param("cartId") int cartId, @Param("status") String status, @Param("modifier") String modifier, @Param("listNumiid") List<String> listNumiid);
}
