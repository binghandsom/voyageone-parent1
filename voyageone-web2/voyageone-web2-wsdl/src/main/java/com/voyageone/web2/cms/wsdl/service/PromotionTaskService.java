package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionTaskDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionTaskModel;
import com.voyageone.web2.sdk.api.request.PromotionTaskAddRequest;
import com.voyageone.web2.sdk.api.response.PromotionTaskAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionTaskService extends BaseService {

    @Autowired
    private CmsPromotionTaskDao cmsPromotionTaskDao;

    public PromotionTaskAddResponse insertPromotionTask(PromotionTaskAddRequest promotionTaskAddRequest){
        PromotionTaskAddResponse res=new PromotionTaskAddResponse();
        res.setInsertedCount(cmsPromotionTaskDao.insertPromotionTask(promotionTaskAddRequest.getCmsBtPromotionTaskModel()));
        return res;
    }
}
