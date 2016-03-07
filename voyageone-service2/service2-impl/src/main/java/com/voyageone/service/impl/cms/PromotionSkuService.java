package com.voyageone.service.impl.cms;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsPromotionSkuDao;
import com.voyageone.web2.sdk.api.domain.CmsBtInventoryOutputTmpModel;
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
public class PromotionSkuService extends BaseService {

    @Autowired
    private CmsPromotionSkuDao cmsPromotionSkuDao;

    public PromotionSkuGetResponse getPromotionSkuList(PromotionSkuGetRequest promotionSkuRequest){
        PromotionSkuGetResponse res=new PromotionSkuGetResponse();
        List<Map<String,Object>> list=cmsPromotionSkuDao.getPromotionSkuList(promotionSkuRequest.getParam());
        if(!CollectionUtils.isEmpty(list)){
            res.setSkus(list);
            res.setTotalCount(Long.parseLong(String.valueOf(list.size())));
        }
        return res;
    }

    public PromotionSkuCountResponse getPromotionSkuListCnt(PromotionSkuCountRequest promotionSkuCountRequest){
        PromotionSkuCountResponse res=new PromotionSkuCountResponse();
        res.setTotalCount(cmsPromotionSkuDao.getPromotionSkuListCnt(promotionSkuCountRequest.getParam()));
        return res;
    }

    @Transactional
    public PromotionSkuDeleteResponse remove(PromotionSkuDeleteRequest promotionSkuDeleteRequest){
        PromotionSkuDeleteResponse res=new PromotionSkuDeleteResponse();
        res.setRemovedCount(cmsPromotionSkuDao.deletePromotionSkuByProductId (promotionSkuDeleteRequest.getPromotionId(),promotionSkuDeleteRequest.getProductId()));
        return res;
    }

    @Transactional
    public PromotionSkuInventoryInfoDeleteResponse delSkuInventoryInfo(PromotionSkuInventoryInfoDeleteRequest request) {
        PromotionSkuInventoryInfoDeleteResponse response=new PromotionSkuInventoryInfoDeleteResponse();
        response.setDelete(cmsPromotionSkuDao.delSkuInventoryInfo());
        return response;
    }

    @Transactional
    public PromotionSkuInventoryInfoInsertResponse insertSkuInventoryInfo(PromotionSkuInventoryInfoInsertRequest request) {
        PromotionSkuInventoryInfoInsertResponse res=new PromotionSkuInventoryInfoInsertResponse();
        res.setInsert(cmsPromotionSkuDao.insertSkuInventoryInfo(request.getInsertRecString()));
        return  res;
    }

    public PromotionSkuInventoryInfoGetCountResponse getSkuInventoryInfoRecCount(PromotionSkuInventoryInfoGetRequest request) {
        PromotionSkuInventoryInfoGetCountResponse res=new PromotionSkuInventoryInfoGetCountResponse();
        res.setTotalCount(cmsPromotionSkuDao.getSkuInventoryInfoRecCount());
        return res;
    }

    public PromotionSkuInventoryInfoGetResponse getSkuInventoryInfoRecInfo(PromotionSkuInventoryInfoGetRequest request) {
        PromotionSkuInventoryInfoGetResponse res=new PromotionSkuInventoryInfoGetResponse();
        List<CmsBtInventoryOutputTmpModel> models=cmsPromotionSkuDao.getSkuInventoryInfoRecInfo((request.getPageNo()-1)*request.getPageSize(),request.getPageSize());
        if(!CollectionUtils.isEmpty(models)){
            res.setModels(models);
            res.setTotalCount(Long.parseLong(String.valueOf(models.size())));
        }
        return res;
    }
}
