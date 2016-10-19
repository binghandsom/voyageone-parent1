package com.voyageone.service.daoext.cms;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtSxCnSkuDaoExt {

    int deleteByListCodes(@Param("channelId") String channelId, @Param("listCodes") List<String> listCodes);
}
