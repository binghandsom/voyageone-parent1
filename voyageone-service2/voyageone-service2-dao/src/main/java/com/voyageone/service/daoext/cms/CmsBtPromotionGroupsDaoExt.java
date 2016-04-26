package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtPromotionGroupsBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsBtPromotionGroupsDaoExt extends ServiceBaseDao {

    public List<CmsBtPromotionGroupsBean> selectPromotionModelList(Map<String, Object> params) {
        List<CmsBtPromotionGroupsBean> ret = selectList("select_cms_bt_promotion_model", params);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }

    public List<Map<String, Object>> selectPromotionModelDetailList(Map<String, Object> params) {
        List<Map<String, Object>> ret = selectList("select_promotion_detail", params);
        if (ret == null) {
            ret = new ArrayList<>();
        }
        return ret;
    }

    public int selectPromotionModelDetailListCnt(Map<String, Object> params) {
        return selectOne("select_promotion_detail_cnt", params);
    }

    public int insertPromotionModel(CmsBtPromotionGroupsBean params) {
        return insert("insert_cms_bt_promotion_model", params);
    }

    public int updatePromotionModel(CmsBtPromotionGroupsBean params) {
        return update("update_cms_bt_promotion_model", params);
    }

    public int deleteCmsPromotionModel(CmsBtPromotionGroupsBean params) {
        return delete("delete_cms_bt_promotion_model", params);
    }

}
