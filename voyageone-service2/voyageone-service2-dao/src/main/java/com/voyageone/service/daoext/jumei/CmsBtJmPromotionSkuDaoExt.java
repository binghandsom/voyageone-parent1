package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionSkuModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmPromotionSkuDaoExt {
    public CmsBtJmPromotionSkuModel getBySkuCodeChannelIdCmsBtJmPromotionId(@Param("skuCode") String skuCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int CmsBtJmPromotionId);
}
