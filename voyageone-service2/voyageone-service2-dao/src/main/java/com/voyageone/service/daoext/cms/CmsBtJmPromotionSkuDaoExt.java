package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionSkuDaoExt {

    public List<Map<String, Object>> getJmSkuPriceInfoListByPromotionId(@Param("promotionId") int promotionId);
    public int updateDealPrice(@Param("dealPrice") BigDecimal dealPrice, @Param("productId") int productId);
    public List getListCmsBtJmImportSkuByPromotionId(int promotionId);
    public int deleteByPromotionId(int promotionId);
    public int  deleteByProductIdListInfo(ProductIdListInfo parameter );

     //jm2 begin

    public CmsBtJmPromotionSkuModel getBySkuCodeChannelIdCmsBtJmPromotionId(@Param("skuCode") String skuCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int CmsBtJmPromotionId);

    //jm2 end
}
