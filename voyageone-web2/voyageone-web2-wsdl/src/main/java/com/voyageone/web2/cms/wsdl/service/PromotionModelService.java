package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionModelDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionGroupModel;
import com.voyageone.web2.sdk.api.request.PromotionDetailAddRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelCountGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionModelsGetRequest;
import com.voyageone.web2.sdk.api.response.PromotionDetailPutResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelCountGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelDeleteResponse;
import com.voyageone.web2.sdk.api.response.PromotionModelsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionModelService extends BaseService {

    @Autowired
    private CmsPromotionModelDao cmsPromotionModelDao;

    public PromotionModelsGetResponse getPromotionModelDetailList(PromotionModelsGetRequest promotionModelsGetRequest) {
        PromotionModelsGetResponse res=new PromotionModelsGetResponse();
        List<Map<String,Object>> models=cmsPromotionModelDao.getPromotionModelDetailList(promotionModelsGetRequest.getParam());
        if(!CollectionUtils.isEmpty(models)) {
            res.setPromotionGroups(models);
            res.setTotalCount(Long.parseLong(models.size()+""));
        }
        return res;
    }

    public PromotionModelCountGetResponse getPromotionModelDetailListCnt(PromotionModelCountGetRequest promotionModelCountGetRequest){
        PromotionModelCountGetResponse res=new PromotionModelCountGetResponse();
        res.setCount(cmsPromotionModelDao.getPromotionModelDetailListCnt(promotionModelCountGetRequest.getParam()));
        return res;
    }

    @Transactional
    public PromotionModelDeleteResponse deleteCmsPromotionModel(PromotionModelDeleteRequest promotionModelDeleteRequest){
        PromotionModelDeleteResponse res=new PromotionModelDeleteResponse();
        res.setRemovedCount(cmsPromotionModelDao.deleteCmsPromotionModel(promotionModelDeleteRequest.getModel()));
        return res;
    }

}
