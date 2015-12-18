package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionSkuModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsPromotionSkuDao extends BaseDao{

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
        return updateTemplate.insert("insert_cms_bt_promotion_sku",params);
    }

    public int updatePromotionSku(CmsBtPromotionSkuModel params){
        return updateTemplate.update("update_cms_bt_promotion_sku", params);
    }

}
