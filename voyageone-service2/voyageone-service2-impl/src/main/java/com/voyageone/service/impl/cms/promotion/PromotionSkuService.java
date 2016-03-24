package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsBtPromotionSkuDao;
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
    private CmsBtPromotionSkuDao cmsPromotionSkuDao;

    public List<Map<String,Object>> getPromotionSkuList(Map<String,Object> params){
        return cmsPromotionSkuDao.selectPromotionSkuList(params);
    }

    public int getPromotionSkuListCnt(Map<String,Object> params){
        return cmsPromotionSkuDao.selectPromotionSkuListCnt(params);
    }

    public int remove(int promotionId, long productId){
        return cmsPromotionSkuDao.deletePromotionSkuByProductId(promotionId, productId);
    }
}
