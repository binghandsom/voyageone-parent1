package com.voyageone.service.daoext.cms;
import com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail.SkuPromotionPriceInfo;
import org.springframework.stereotype.Repository;
import java.util.Map;
@Repository
public interface CmsBtPromotionSkusDaoExtCamel {
    int deleteByPromotionId(int promotionId);

    int deleteByPromotionCodeList(Map<String, Object> map);

    int updatePromotionPrice(SkuPromotionPriceInfo parameter);
}