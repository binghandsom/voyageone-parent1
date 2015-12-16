package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtPromotionGroupModel;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsPromotionModelDao extends BaseDao{

    public List<CmsBtPromotionGroupModel> getPromotionModelList(Map<String,Object> params){
        List<CmsBtPromotionGroupModel> ret = selectList("select_cms_bt_promotion_model",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
    public int insertPromotionModel(CmsBtPromotionGroupModel params){
        return updateTemplate.insert("insert_cms_bt_promotion_model",params);
    }

    public int updatePromotionModel(CmsBtPromotionGroupModel params){
        return updateTemplate.update("update_cms_bt_promotion_model", params);
    }

}
