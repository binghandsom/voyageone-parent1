package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtPromotionGroupModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsBtPromotionModelDao extends ServiceBaseDao {

    public List<CmsBtPromotionGroupModel> selectPromotionModelList(Map<String, Object> params){
        List<CmsBtPromotionGroupModel> ret = selectList("select_cms_bt_promotion_model",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }

    public List<Map<String,Object>> selectPromotionModelDetailList(Map<String, Object> params) {
        List<Map<String,Object>> ret = selectList("select_promotion_detail", params);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }
    public int selectPromotionModelDetailListCnt(Map<String, Object> params){
        return selectOne("select_promotion_detail_cnt",params);
    }
    public int insertPromotionModel(CmsBtPromotionGroupModel params){
        return insert("insert_cms_bt_promotion_model", params);
    }

    public int updatePromotionModel(CmsBtPromotionGroupModel params){
        return update("update_cms_bt_promotion_model", params);
    }

    public int deleteCmsPromotionModel(CmsBtPromotionGroupModel params){
        return delete("delete_cms_bt_promotion_model",params);
    }

}
