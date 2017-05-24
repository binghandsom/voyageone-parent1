package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsBtPromotionCodesDaoExt extends ServiceBaseDao {

    public List<CmsBtPromotionCodesBean> selectPromotionCodeList(Map<String, Object> params) {
        return selectList("select_cms_bt_promotion_code", params);
    }

    public List<CmsBtPromotionCodesBean> selectPromotionCodeSkuList(Map<String, Object> params) {
        return selectList("select_cms_bt_promotion_code_sku", params);
    }
    public List<CmsBtPromotionCodesBean> selectPromotionCodeSku2List(Map<String, Object> params) {
        return selectList("select_cms_bt_promotion_code_sku2", params);
    }

    public int selectPromotionCodeListCnt(Map<String, Object> params) {
        return selectOne("select_cms_bt_promotion_code_cnt", params);
    }

    public int insertPromotionCode(CmsBtPromotionCodesBean params) {
        return insert("insert_cms_bt_promotion_code", params);
    }

    public int updatePromotionCode(CmsBtPromotionCodesBean params) {
        return update("update_cms_bt_promotion_code", params);
    }

    public int deletePromotionCode(CmsBtPromotionCodesBean params) {
        return delete("delete_cms_bt_promotion_code", params);
    }

    public int deletePromotionCodeByModelId(Integer promotionId, String productModel) {
        CmsBtPromotionCodesBean params = new CmsBtPromotionCodesBean();
        params.setPromotionId(promotionId);
        params.setProductModel(productModel);
        return delete("delete_cms_bt_promotion_code", params);
    }

    public List<Map<String, Object>> selectCmsBtPromotionAllCodeByPromotionIdS(List<String> promotionIdList) {
        return selectList("select_cms_bt_promotion_code_pro_promotionIds", promotionIdList);
    }

    public String selectCodeInActivePromotionName(Map<String, Object> params) {
        String promotionNames = "";
        List<Map<String, Object>> promotionList = selectList("select_cms_bt_promotion_code_in_active_promotion_name", params);
        // 拼接PromotionName
        for (Map<String, Object> promotion : promotionList) {
            promotionNames += promotion.get("promotion_name") + ",";
        }
        if (!StringUtils.isEmpty(promotionNames)) {
            promotionNames = promotionNames.substring(0,promotionNames.length() - 1);
        }
        return promotionNames;
    }
    public int selectCmsBtPromotionCodeInPromtionCnt(String code, List<Integer> promotionIds){
        Map<String,Object> params = new HashMap();
        params.put("code", code);
        params.put("promotionIds", promotionIds);
        return selectOne("select_cms_bt_promotion_code_in_promtion_cnt",params);
    }

}
