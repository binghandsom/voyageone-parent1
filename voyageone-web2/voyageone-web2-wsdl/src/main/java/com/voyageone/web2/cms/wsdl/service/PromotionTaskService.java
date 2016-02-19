package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionTaskDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
import com.voyageone.web2.sdk.api.request.PromotionTaskAddRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceGetCountRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceGetRequest;
import com.voyageone.web2.sdk.api.request.PromotionTaskPriceUpdateRequest;
import com.voyageone.web2.sdk.api.response.PromotionTaskAddResponse;
import com.voyageone.web2.sdk.api.response.PromotionTaskPriceGetCountResponse;
import com.voyageone.web2.sdk.api.response.PromotionTaskPriceGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionTaskPriceUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionTaskService extends BaseService {

    @Autowired
    private CmsPromotionTaskDao cmsPromotionTaskDao;

    @Transactional
    public PromotionTaskAddResponse insertPromotionTask(PromotionTaskAddRequest promotionTaskAddRequest){
        PromotionTaskAddResponse res=new PromotionTaskAddResponse();
        res.setInsertedCount(cmsPromotionTaskDao.insertPromotionTask(promotionTaskAddRequest.getCmsBtPromotionTaskModel()));
        return res;
    }

    public PromotionTaskPriceGetCountResponse getPromotionTaskPriceListCnt(PromotionTaskPriceGetCountRequest request){
        PromotionTaskPriceGetCountResponse res=new PromotionTaskPriceGetCountResponse();
        res.setTotalCount(cmsPromotionTaskDao.getPromotionTaskListCnt(request.getParams()));
        return res;
    }

    public PromotionTaskPriceGetResponse getPromotionTaskPriceList(PromotionTaskPriceGetRequest request){
        PromotionTaskPriceGetResponse res=new PromotionTaskPriceGetResponse();
        List<Map<String,Object>> datas=cmsPromotionTaskDao.getPromotionTaskPriceList(request.getParams());
        if(!CollectionUtils.isEmpty(datas)){
            res.setPromotionTaskPrices(datas);
            res.setTotalCount(Long.parseLong(String.valueOf(datas.size())));
        }
        return res;
    }

    @Transactional
    public PromotionTaskPriceUpdateResponse updatePromotionTask(PromotionTaskPriceUpdateRequest request){
        PromotionTaskPriceUpdateResponse res=new PromotionTaskPriceUpdateResponse();
        res.setModifiedCount(cmsPromotionTaskDao.updatePromotionTask(request.getParam()));
        return res;
    }
}
