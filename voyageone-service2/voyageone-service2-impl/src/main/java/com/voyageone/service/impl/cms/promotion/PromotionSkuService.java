package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail.SaveSkuPromotionPricesParameter;
import com.voyageone.service.dao.cms.CmsBtPromotionSkusDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionSkusDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionSkusDaoExtCamel;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    private CmsBtPromotionSkusDaoExt cmsPromotionSkuDao;
    @Autowired
    CmsBtPromotionSkusDao cmsBtPromotionSkusDao;
    @Autowired
    CmsBtPromotionSkusDaoExtCamel cmsBtPromotionSkusDaoExtCamel;
    public List<Map<String, Object>> getPromotionSkuList(Map<String, Object> params) {
        return cmsPromotionSkuDao.selectPromotionSkuList(params);
    }
    public int getPromotionSkuListCnt(Map<String, Object> params) {
        return cmsPromotionSkuDao.selectPromotionSkuListCnt(params);
    }
    @VOTransactional
    public int remove(int promotionId, long productId) {
        return cmsPromotionSkuDao.deletePromotionSkuByProductId(promotionId, productId);
    }
    public List<Map<String, Object>> getCmsBtPromotionSkuByPromotionIds(List<String> promotionIdList) {
        return cmsPromotionSkuDao.selectCmsBtPromotionSkuByPromotionIds(promotionIdList);
    }
    public List<CmsBtPromotionSkusModel> getListByWhere(Map<String, Object> map) {
//        Map<String, Object> map = new HashedMap();
//        map.put("promotionId", promotion_id);
//        map.put("productId", product_id);
        return cmsBtPromotionSkusDao.selectList(map);
    }
    public CmsBtPromotionSkusModel get(int promotionId,String productCode,String productSku) {
        Map<String, Object> map = new HashedMap();
        map.put("promotionId", promotionId);
        map.put("productCode", productCode);
        map.put("productSku", productSku);
        return cmsBtPromotionSkusDao.selectOne(map);
    }
    @VOTransactional
    public void saveSkuPromotionPrices(List<SaveSkuPromotionPricesParameter> list) {
        list.forEach((p) -> {
            cmsBtPromotionSkusDaoExtCamel.updatePromotionPrice(p);
        });
    }
}
