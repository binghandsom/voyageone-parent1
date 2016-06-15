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

    List getListByWhere(Map<String, Object> map);

    CmsBtJmPromotionProductModel getByProductCodeChannelIdCmsBtJmPromotionId(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    List getExportInfoListByPromotionId(int promotionId);

    List getListCmsBtJmImportProductByPromotionId(int promotionId);

    int deleteByPromotionId(int promotionId);

    int deleteByProductIdListInfo(ProductIdListInfo parameter);

    int jmNewUpdateAll(int promotionId);

    int jmNewByProductIdListInfo(ProductIdListInfo parameter);

    int updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter);

    int updateDealEndTime(ParameterUpdateDealEndTime parameter);

    //jm2 begin
    List getPageByWhere(Map<String, Object> map);//add

    int getCountByWhere(Map<String, Object> ma);//add

    //add
    Boolean existsCode(@Param("channelId") String channelId, @Param("productCode") String productCode, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

    CmsBtJmPromotionProductModel getByProductCode(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    List getExportListByPromotionId(int promotionId);

    int batchUpdateDealPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId, @Param("dealPrice") String dealPrice);

    int batchSynchPrice(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int synchAllPrice(int promotionId);

    int batchCopyDeal(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int copyDealAll(int promotionId);

    int batchDeleteProduct(@Param("listPromotionProductId") List<Long> listPromotionProductId);

    int deleteAllProduct(int promotionId);

    List<CmsBtJmPromotionProductModel> getJMCopyList(int promotionId);


    /**
     * 获取jm_hash_id
     * @param productCode
     * @param channelId
     * @return
     */
    List<String> getJmHashIds (@Param("channelId") String channelId, @Param("productCode") String productCode);
    //jm2 end
}
