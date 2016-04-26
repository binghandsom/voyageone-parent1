package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

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
}
