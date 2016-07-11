package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.jumei.SkuPriceBean;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionSkuDaoExt {

    CmsBtJmPromotionSkuModel selectBySkuCodeChannelIdCmsBtJmPromotionId(@Param("skuCode") String skuCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int CmsBtJmPromotionId);

    List<Map<String, Object>> selectJmSkuPriceInfoListByPromotionId(@Param("promotionId") int promotionId);

    int updateDealPrice(@Param("dealPrice") BigDecimal dealPrice, @Param("productId") int productId);

    List<Map<String, Object>> selectListCmsBtJmImportSkuByPromotionId(int promotionId);

    int deleteByPromotionId(int promotionId);

    int deleteByProductIdListInfo(ProductIdListInfo parameter);

    //jm2 begin                                                                                                                       cms_bt_jm_promotion_product_id
    CmsBtJmPromotionSkuModel selectBySkuCode(@Param("skuCode") String skuCode, @Param("cmsBtJmPromotionProductId") int cmsBtJmPromotionProductId);

    List<Map<String, Object>> selectExportListByPromotionId(int promotionId);
   //更新的dealPrice大于market_price的记录
    CmsBtJmPromotionSkuModel  selectNotUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    int batchUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    List<SkuPriceBean> selectJmSkuPriceInfoListByPromotionProductId(int promotionProductId);

    int batchDeleteSku(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int deleteAllSku(@Param("promotionId") int promotionId);
    List<MapModel> selectViewListByPromotionProductId(int promotionProductId);
    //jm2 end
}
