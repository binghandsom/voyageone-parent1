package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtInventoryOutputTmpModel;
import com.voyageone.service.model.cms.CmsBtPromotionSkuModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsBtPromotionSkuDao extends ServiceBaseDao {

    public List<Map<String,Object>> selectPromotionSkuList(Map<String, Object> params){
        List<Map<String,Object>> ret = selectList("select_cms_bt_promotion_sku",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
    public int selectPromotionSkuListCnt(Map<String, Object> params){
        return selectOne("select_cms_bt_promotion_sku_cnt",params);
    }
    public int insertPromotionSku(CmsBtPromotionSkuModel params){
        return insert("insert_cms_bt_promotion_sku", params);
    }

    public int updatePromotionSku(CmsBtPromotionSkuModel params){
        return update("update_cms_bt_promotion_sku", params);
    }
    public int deletePromotionSku(CmsBtPromotionSkuModel params){
        return delete("delete_cms_bt_promotion_sku", params);
    }

    public int deletePromotionSkuByModelId(Integer promotionId, String  productModel){
        CmsBtPromotionSkuModel params = new CmsBtPromotionSkuModel();
        params.setPromotionId(promotionId);
        params.setProductModel(productModel);
        return delete("delete_cms_bt_promotion_sku", params);
    }
    public int deletePromotionSkuByProductId(Integer promotionId, Long productId){
        CmsBtPromotionSkuModel params = new CmsBtPromotionSkuModel();
        params.setPromotionId(promotionId);
        params.setProductId(productId);
        return delete("delete_cms_bt_promotion_sku", params);
    }

    public int deletePromotionSkuByProductCode(Integer promotionId, String productCode){
        CmsBtPromotionSkuModel params = new CmsBtPromotionSkuModel();
        params.setPromotionId(promotionId);
        params.setProductCode(productCode);
        return delete("delete_cms_bt_promotion_sku", params);
    }
}
