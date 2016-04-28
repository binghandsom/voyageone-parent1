package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTime;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionProductDaoExt {
    public CmsBtJmPromotionProductModel getByProductCodeChannelIdCmsBtJmPromotionId(@Param("productCode") String productCode, @Param("channelId") String channelId, @Param("cmsBtJmPromotionId") int cmsBtJmPromotionId);

    public List getListByWhere(Map<String, Object> map);

    public List getPromotionProductInfoListByWhere(Map<String, Object> map);

    public int getPromotionProductInfoCountByWhere(Map<String, Object> ma);

    public List getExportInfoListByPromotionId(int promotionId);

    public List getListCmsBtJmImportProductByPromotionId(int promotionId);

    public int deleteByPromotionId(int promotionId);

    public int deleteByProductIdListInfo(ProductIdListInfo parameter);

    public int jmNewUpdateAll(int promotionId);

    public int jmNewByProductIdListInfo(ProductIdListInfo parameter);

    public int updateDealEndTimeAll( ParameterUpdateDealEndTimeAll parameter);

    public int updateDealEndTime(ParameterUpdateDealEndTime parameter);
}
