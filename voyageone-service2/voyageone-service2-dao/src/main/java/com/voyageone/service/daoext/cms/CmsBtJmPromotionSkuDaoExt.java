package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.jumei2.SkuPriceBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionSkuDaoExt {

    CmsBtJmPromotionSkuModel getBySkuCodeChannelIdCmsBtJmPromotionId(@Param("skuCode") String skuCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int CmsBtJmPromotionId);

    List<Map<String, Object>> getJmSkuPriceInfoListByPromotionId(@Param("promotionId") int promotionId);

    int updateDealPrice(@Param("dealPrice") BigDecimal dealPrice, @Param("productId") int productId);

    List getListCmsBtJmImportSkuByPromotionId(int promotionId);

    int deleteByPromotionId(int promotionId);

    int deleteByProductIdListInfo(ProductIdListInfo parameter);

    //jm2 begin                                                                                                                       cms_bt_jm_promotion_product_id
    CmsBtJmPromotionSkuModel getBySkuCode(@Param("skuCode") String skuCode, @Param("cmsBtJmPromotionProductId") int cmsBtJmPromotionProductId);

    List getExportListByPromotionId(int promotionId);

    int batchUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    List<SkuPriceBean> getJmSkuPriceInfoListByPromotionProductId(int promotionProductId);

    int batchDeleteSku(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int deleteAllSku(@Param("promotionId") int promotionId);
    List getViewListByPromotionProductId(int promotionProductId);
    //jm2 end
}
