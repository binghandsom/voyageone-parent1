package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmSkuDaoExt {
    CmsBtJmSkuModel selectBySkuCodeChannelId(@Param("skuCode") String skuCode,@Param("productCode") String productCode, @Param("channelId") String channelId);
    Boolean existsCode(@Param("skuCode") String skuCode,@Param("productCode") String productCode, @Param("channelId") String channelId);
}
