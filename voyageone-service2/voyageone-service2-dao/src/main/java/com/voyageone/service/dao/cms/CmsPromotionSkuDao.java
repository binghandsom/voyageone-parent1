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
//Repository
public class CmsPromotionSkuDao extends ServiceBaseDao {

    public List<Map<String,Object>> getPromotionSkuList(Map<String,Object> params){
        List<Map<String,Object>> ret = selectList("select_cms_bt_promotion_sku",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
    public int getPromotionSkuListCnt(Map<String,Object> params){
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

    public int deletePromotionSkuByModelId(Integer promotionId, Long modelId){
        CmsBtPromotionSkuModel params = new CmsBtPromotionSkuModel();
        params.setPromotionId(promotionId);
        params.setModelId(modelId);
        return delete("delete_cms_bt_promotion_sku", params);
    }
    public int deletePromotionSkuByProductId(Integer promotionId, Long productId){
        CmsBtPromotionSkuModel params = new CmsBtPromotionSkuModel();
        params.setPromotionId(promotionId);
        params.setProductId(productId);
        return delete("delete_cms_bt_promotion_sku", params);
    }

//    public boolean insertSkuInventoryInfo(String values) {
//        boolean ret = true;
//
//        Map<String, String> dataMap = new HashMap<String, String>();
//        dataMap.put("values", values);
//
//        int retCount = insert("wms_bt_inventory_center_output_tmp_insertSKUData", dataMap);
//
//        return  ret;
//    }
//
//    public boolean delSkuInventoryInfo() {
//        delete("wms_bt_inventory_center_output_tmp_deleteAll");
//
//        return true;
//    }
//
//    public int getSkuInventoryInfoRecCount() {
//        return selectOne("wms_bt_inventory_center_output_tmp_getRecCount");
//    }
//
//    public List<CmsBtInventoryOutputTmpModel> getSkuInventoryInfoRecInfo(int offset,int pagesize) {
//        Map<String, Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("offset", offset * pagesize);
//        dataMap.put("pagesize", pagesize);
//
//        List<CmsBtInventoryOutputTmpModel> ret = selectList("wms_bt_inventory_center_output_tmp_getRecInfo", dataMap);
//
//        return  ret;
//    }
}
