package com.voyageone.web2.cms.views.promotion.list;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.request.PromotionDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionPutRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.util.SdkBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionIndexService extends BaseAppService {

    @Autowired
    VoApiDefaultClient voApiClient;


    /**
     * 获取该channel的category类型.
     * @param channelId
     * @param language
     * @return
     */
    public Map<String, Object> init (String channelId, String language) {
        Map<String, Object> result = new HashMap<>();

        result.put("platformTypeList", TypeChannel.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));

        return result;
    }

    public CmsBtPromotionModel queryById(Integer promotionId) {
        PromotionsGetRequest request=new PromotionsGetRequest();
        request.setPromotionId(promotionId);
        List<CmsBtPromotionModel> models=voApiClient.execute(request).getCmsBtPromotionModels();
        if(models!=null&& models.size()==1){
            return models.get(0);
        }else {
            return null;
        }
    }

    public List<CmsBtPromotionModel> queryByCondition(Map<String, Object> conditionParams) {
        PromotionsGetRequest request=new PromotionsGetRequest();
        SdkBeanUtils.copyProperties(conditionParams, request);
        return voApiClient.execute(request).getCmsBtPromotionModels();
    }

    public int addOrUpdate(CmsBtPromotionModel cmsBtPromotionModel) {
        PromotionPutRequest request=new PromotionPutRequest();
        request.setCmsBtPromotionModel(cmsBtPromotionModel);
        if(cmsBtPromotionModel.getPromotionId()!=null){
            return voApiClient.execute(request).getModifiedCount();
        }else{
            return voApiClient.execute(request).getInsertedCount();
        }
    }

    public int deleteById(Integer promotionId) {
        PromotionDeleteRequest request=new PromotionDeleteRequest();
        request.setPromotionId(promotionId);
        return voApiClient.execute(request).getRemovedCount();
    }

}
