package com.voyageone.service.daoext.cms;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTime;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionProductDaoExt {

    List selectListByWhere(Map<String, Object> map);

    CmsBtJmPromotionProductModel selectByProductCodeChannelIdCmsBtJmPromotionId(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    List selectExportInfoListByPromotionId(int promotionId);

    List selectListCmsBtJmImportProductByPromotionId(int promotionId);

    int deleteByPromotionId(int promotionId);

    int deleteByProductIdListInfo(ProductIdListInfo parameter);

    int jmNewUpdateAll(int promotionId);

    int jmNewByProductIdListInfo(ProductIdListInfo parameter);

    int updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter);

    int updateDealEndTime(ParameterUpdateDealEndTime parameter);

    //jm2 begin
    List selectPageByWhere(Map<String, Object> map);//add

    int selectCountByWhere(Map<String, Object> ma);//add

    CmsBtJmPromotionProductModel selectDateRepeatByCode(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    //add  不包含本次活动
    Boolean existsCode(@Param("cmsBtJmPromotionId") int cmsBtJmPromotionId, @Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    CmsBtJmPromotionProductModel selectByProductCode(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    List selectExportListByPromotionId(int promotionId);

    int batchUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    int batchSynchPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int synchAllPrice(int promotionId);

    int batchCopyDeal(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int copyDealAll(int promotionId);

    int batchDeleteProduct(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int deleteAllProduct(int promotionId);

    List<CmsBtJmPromotionProductModel> selectJMCopyList(int promotionId);
    /**
     * 获取jm_hash_id
     *
     * @param productCode
     * @param channelId
     * @return
     */
    List<String> selectJmHashIds(@Param("channelId") String channelId, @Param("productCode") String productCode);
    //是否存在在销售的商品
    CmsBtJmPromotionProductModel getOnSaleByCode(@Param("channelId") String channelId, @Param("productCode") String productCode);
    int updateAvgPriceByPromotionProductId(long cmsBtJmPromotionProductId);
//获取变更数量
    int selectChangeCountByPromotionId(long cmsBtJmPromotionProductId);
    //jm2 end
}
