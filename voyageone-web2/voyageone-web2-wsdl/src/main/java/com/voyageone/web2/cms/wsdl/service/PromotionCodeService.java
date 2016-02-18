package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionCodeDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import com.voyageone.web2.sdk.api.request.PromotionCodeDeleteRequest;
import com.voyageone.web2.sdk.api.request.PromotionCodeGetCountRequest;
import com.voyageone.web2.sdk.api.request.PromotionCodeGetRequest;
import com.voyageone.web2.sdk.api.response.PromotionCodeDeleteResponse;
import com.voyageone.web2.sdk.api.response.PromotionCodeGetCountResponse;
import com.voyageone.web2.sdk.api.response.PromotionCodeGetResponse;
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
public class PromotionCodeService extends BaseService {

    @Autowired
    private CmsPromotionCodeDao cmspromotionCodeDao;

    public PromotionCodeGetResponse getPromotionCodeList(PromotionCodeGetRequest promotionCodeGetRequest){
        PromotionCodeGetResponse res=new PromotionCodeGetResponse();
        List<CmsBtPromotionCodeModel> ret = cmspromotionCodeDao.getPromotionCodeList(promotionCodeGetRequest.getParam());
        if(!CollectionUtils.isEmpty(ret)){
            res.setCodeList(ret);
            res.setTotalCount(Long.parseLong(String.valueOf(ret.size())));
        }
        return res;
    }

    public PromotionCodeGetCountResponse getPromotionCodeListCnt(PromotionCodeGetCountRequest promotionCodeGetCountRequest){
        PromotionCodeGetCountResponse res=new PromotionCodeGetCountResponse();
        res.setTotalCount(cmspromotionCodeDao.getPromotionCodeListCnt(promotionCodeGetCountRequest.getParam()));
        return res;
    }

    @Transactional
    public PromotionCodeDeleteResponse deletePromotionCode(PromotionCodeDeleteRequest promotionCodeDeleteRequest){
        PromotionCodeDeleteResponse res=new PromotionCodeDeleteResponse();
        res.setRemovedCount(cmspromotionCodeDao.deletePromotionCode(promotionCodeDeleteRequest.getModel()));
        return res;
    }

}
