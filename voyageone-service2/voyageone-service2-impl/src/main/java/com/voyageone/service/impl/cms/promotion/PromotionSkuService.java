package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsPromotionSkuDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
//
//    @Autowired
//    private WmsBtInventoryCenterOutputTmpDao wmsBtInventoryCenterOutputTmpDao;
//
    public List<Map<String,Object>> getPromotionSkuList(Map<String,Object> params){
        return cmsPromotionSkuDao.getPromotionSkuList(params);
    }

    public int getPromotionSkuListCnt(Map<String,Object> params){
        return cmsPromotionSkuDao.getPromotionSkuListCnt(params);
    }

    public int remove(int promotionId, long productId){
        return cmsPromotionSkuDao.deletePromotionSkuByProductId(promotionId, productId);
    }
//
//    @Transactional
//    public PromotionSkuInventoryInfoDeleteResponse delSkuInventoryInfo(PromotionSkuInventoryInfoDeleteRequest request) {
//        PromotionSkuInventoryInfoDeleteResponse response=new PromotionSkuInventoryInfoDeleteResponse();
//        response.setDelete(wmsBtInventoryCenterOutputTmpDao.delSkuInventoryInfo());
//        return response;
//    }
//
//    @Transactional
//    public PromotionSkuInventoryInfoInsertResponse insertSkuInventoryInfo(PromotionSkuInventoryInfoInsertRequest request) {
//        PromotionSkuInventoryInfoInsertResponse res=new PromotionSkuInventoryInfoInsertResponse();
//        res.setInsert(wmsBtInventoryCenterOutputTmpDao.insertSkuInventoryInfo(request.getInsertRecString()));
//        return  res;
//    }
//
//    public PromotionSkuInventoryInfoGetCountResponse getSkuInventoryInfoRecCount(PromotionSkuInventoryInfoGetRequest request) {
//        PromotionSkuInventoryInfoGetCountResponse res=new PromotionSkuInventoryInfoGetCountResponse();
//        res.setTotalCount(wmsBtInventoryCenterOutputTmpDao.getSkuInventoryInfoRecCount());
//        return res;
//    }
//
//    public PromotionSkuInventoryInfoGetResponse getSkuInventoryInfoRecInfo(PromotionSkuInventoryInfoGetRequest request) {
//        PromotionSkuInventoryInfoGetResponse res=new PromotionSkuInventoryInfoGetResponse();
//        List<CmsBtInventoryOutputTmpModel> models = wmsBtInventoryCenterOutputTmpDao.getSkuInventoryInfoRecInfo((request.getPageNo()-1)*request.getPageSize(),request.getPageSize());
//        if(!CollectionUtils.isEmpty(models)){
//            res.setModels(models);
//            res.setTotalCount(Long.parseLong(String.valueOf(models.size())));
//        }
//        return res;
//    }
}
