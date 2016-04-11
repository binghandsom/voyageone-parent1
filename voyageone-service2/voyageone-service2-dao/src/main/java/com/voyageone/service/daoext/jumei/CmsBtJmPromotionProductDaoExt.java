package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionProductDaoExt {
    public CmsBtJmPromotionProductModel getByProductCodeChannelIdCmsBtJmPromotionId(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    public List getListByWhere(Map<String, Object> map);

    public List getPromotionProductInfoListByWhere(Map<String, Object> map);

    public List getExportInfoListByPromotionId(int promotionId);
}
