package com.voyageone.service.daoext.jumei;

import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionSkuDaoExt {
    public CmsBtJmPromotionSkuModel getBySkuCodeChannelIdCmsBtJmPromotionId(@Param("skuCode") String skuCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int CmsBtJmPromotionId);

    public List<Map<String, Object>> getJmSkuPriceInfoListByPromotionId(@Param("promotionId") int promotionId);

    public int updateDealPrice(@Param("dealPrice") BigDecimal dealPrice, @Param("productId") int productId);

    public List getListCmsBtJmImportSkuByPromotionId(int promotionId);
}
