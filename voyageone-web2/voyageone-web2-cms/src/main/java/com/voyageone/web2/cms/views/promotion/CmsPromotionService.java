package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.request.PromotionsDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionsGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionsPutRequest;
import com.voyageone.web2.sdk.api.request.TagAddRequest;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionService extends BaseAppService {

    @Autowired
    VoApiDefaultClient voApiClient;

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
        BeanUtils.copyProperties(conditionParams,request);
        return voApiClient.execute(request).getCmsBtPromotionModels();
    }

    public int addOrUpdate(CmsBtPromotionModel cmsBtPromotionModel) {
        PromotionsPutRequest request=new PromotionsPutRequest();
        request.setCmsBtPromotionModel(cmsBtPromotionModel);
        if(cmsBtPromotionModel.getPromotionId()!=null){
            return voApiClient.execute(request).getModifiedCount();
        }else{
            return voApiClient.execute(request).getInsertedCount();
        }
    }

    public int deleteById(Integer promotionId) {
        PromotionsDeleteRequest request=new PromotionsDeleteRequest();
        request.setPromotionId(promotionId);
        return voApiClient.execute(request).getRemovedCount();
    }

}
