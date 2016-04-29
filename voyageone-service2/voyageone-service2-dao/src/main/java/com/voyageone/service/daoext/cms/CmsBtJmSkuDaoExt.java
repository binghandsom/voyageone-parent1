package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmSkuDaoExt {
    public  CmsBtJmSkuModel getBySkuCodeChannelId(@Param("skuCode") String skuCode,@Param("channelId") String channelId);
    }
