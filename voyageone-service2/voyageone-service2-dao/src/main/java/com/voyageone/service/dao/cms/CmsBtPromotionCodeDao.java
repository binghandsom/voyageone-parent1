package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
//Repository
public class CmsBtPromotionCodeDao extends BaseDao {

    public List<CmsBtPromotionCodeModel> getPromotionCodeList(Map<String, Object> params) {
        return selectList("select_cms_bt_promotion_code", params);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        return selectOne("select_cms_bt_promotion_code_cnt", params);
    }

    public int insertPromotionCode(CmsBtPromotionCodeModel params) {
        return insert("insert_cms_bt_promotion_code", params);
    }

    public int updatePromotionCode(CmsBtPromotionCodeModel params) {
        return update("update_cms_bt_promotion_code", params);
    }

    public int deletePromotionCode(CmsBtPromotionCodeModel params) {
        return delete("delete_cms_bt_promotion_code", params);
    }

    public int deletePromotionCodeByModelId(Integer promotionId, Long modelId) {
        CmsBtPromotionCodeModel params = new CmsBtPromotionCodeModel();
        params.setPromotionId(promotionId);
        params.setModelId(modelId);
        return delete("delete_cms_bt_promotion_code", params);
    }
}
