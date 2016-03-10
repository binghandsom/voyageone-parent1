package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsBtPromotionTaskModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
//Repository
public class CmsBtPromotionTaskDao extends BaseDao {

    public List<CmsBtPromotionTaskModel> getPromotionTaskList(Map<String,Object> params){
        List<CmsBtPromotionTaskModel> ret = selectList("select_cms_bt_promotion_task",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
//    public int getPromotionTaskListCnt(Map<String,Object> params){
//        return selectOne("select_cms_bt_promotion_code_cnt",params);
//    }

    public int getPromotionTaskPriceListCnt(Map<String,Object> params){
        return selectOne("select_cms_bt_promotion_task_price_cnt",params);
    }

    public List<Map<String,Object>> getPromotionTaskPriceList(Map<String,Object> params){
        return selectList("select_cms_bt_promotion_task_price", params);
    }

    public int insertPromotionTask(CmsBtPromotionTaskModel params){
        return updateTemplate.insert("insert_cms_bt_promotion_task", params);
    }

    public int updatePromotionTask(CmsBtPromotionTaskModel params){
        return updateTemplate.update("update_cms_bt_promotion_task", params);
    }


    public List<CmsBtPromotionTaskModel> getPromotionByCodeNotInAllPromotion(Map<String,Object> params){
        return selectList("select_cms_bt_promotion_task_by_code_in_no_all_promotion",params);
    }
}
