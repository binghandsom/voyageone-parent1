package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceGetCountRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsTaskPriceService extends BaseAppService {

    @Autowired
    protected VoApiDefaultClient voApiClient;

   public List<Map<String,Object>> getPriceList(Map<String,Object> param){
       PromotionTaskPriceGetRequest request=new PromotionTaskPriceGetRequest();
       request.setParams(param);
       return voApiClient.execute(request).getPromotionTaskPrices();
   }

    public int getPriceListCnt(Map<String,Object> param){
        PromotionTaskPriceGetCountRequest request=new PromotionTaskPriceGetCountRequest();
        request.setParams(param);
        return voApiClient.execute(request).getTotalCount();
    }

    public int updateTaskStatus(CmsBtPromotionTaskModel param){
        PromotionTaskPriceUpdateRequest request=new PromotionTaskPriceUpdateRequest();
        request.setParam(param);
        return voApiClient.execute(request).getModifiedCount();
    }
}
