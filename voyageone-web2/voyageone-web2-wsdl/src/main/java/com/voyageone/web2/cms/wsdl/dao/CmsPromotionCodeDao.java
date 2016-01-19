package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionCodeModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsPromotionCodeDao extends BaseDao{

    public List<CmsBtPromotionCodeModel> getPromotionCodeList(Map<String,Object> params){
        List<CmsBtPromotionCodeModel> ret = updateTemplate.selectList("select_cms_bt_promotion_code",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
    public int getPromotionCodeListCnt(Map<String,Object> params){
        return updateTemplate.selectOne("select_cms_bt_promotion_code_cnt",params);
    }
    public int insertPromotionCode(CmsBtPromotionCodeModel params){
        return updateTemplate.insert("insert_cms_bt_promotion_code", params);
    }

    public int updatePromotionCode(CmsBtPromotionCodeModel params){
        return updateTemplate.update("update_cms_bt_promotion_code", params);
    }

    public int deletePromotionCode(CmsBtPromotionCodeModel params){
        return updateTemplate.delete("delete_cms_bt_promotion_code", params);
    }

    public int deletePromotionCodeByModelId(Integer promotionId, Long modelId){
        CmsBtPromotionCodeModel params = new CmsBtPromotionCodeModel();
        params.setPromotionId(promotionId);
        params.setModelId(modelId);
        return updateTemplate.delete("delete_cms_bt_promotion_code", params);
    }

}
