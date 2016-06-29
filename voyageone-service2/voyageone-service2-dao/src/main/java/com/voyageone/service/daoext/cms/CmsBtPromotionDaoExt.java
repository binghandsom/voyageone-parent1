/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtPromotionBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.bean.cms.CmsBtPromotionHistoryBean;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * promotion 数据持久层
 *
 * @author gbb
 */
@Repository
public class CmsBtPromotionDaoExt extends ServiceBaseDao {

    /**
     * 条件查询
     *
     * @param condtionParams condtionParams
     * @return List<CmsBtPromotionModel>
     */
    public List<CmsBtPromotionBean> selectByCondition(Map<?, ?> condtionParams) {
        return selectList("select_cms_bt_promotion_by_condtion", condtionParams);
    }

    /**
     * 条件查询
     *
     * @param condtionParams condtionParams
     * @return List<CmsBtPromotionModel>
     */
    public List<CmsBtPromotionBean> select4AdvSearch(Map<?, ?> condtionParams) {
        return selectList("select_cms_bt_promotion_4_adv_search", condtionParams);
    }

    /**
     * 根据id查询
     *
     * @param condtionParams condtion Params
     * @return CmsBtPromotionModel
     */
    public CmsBtPromotionBean selectById(Map<?, ?> condtionParams) {
        return selectOne("select_cms_bt_promotion_by_id", condtionParams);
    }

    /**
     * 修改
     *
     * @param cmsBtPromotionModel CmsBtPromotionModel
     * @return int
     */
    public int update(CmsBtPromotionModel cmsBtPromotionModel) {
        return update("update_cms_bt_promotion", cmsBtPromotionModel);
    }

    /**
     * 插入
     *
     * @param cmsBtPromotionBean CmsBtPromotionBean
     * @return int
     */
    public int insert(CmsBtPromotionBean cmsBtPromotionBean) {
        return insert("insert_cms_bt_promotion", cmsBtPromotionBean);
    }

    /**
     * 删除(逻辑删除，修改is_active=0)
     *
     * @param condtionParams condtion Params
     * @return int
     */
    public int deleteById(Map<?, ?> condtionParams) {
        return update("delete_cms_bt_promotion_by_id", condtionParams);
    }

    /**
     * 条件查询
     *
     * @param PromotionID PromotionID
     * @return String
     */
    public HashMap selectPromotionIDByCartId(String PromotionID) {
        return selectOne("select_cms_bt_promotion_by_cat_id", PromotionID);
    }

    /**
     * 获取该product参加的promotion履历
     * @param param
     * @return
     */
    public List<CmsBtPromotionHistoryBean> selectPromotionHistory(Map<String, Object> param) {
        return updateTemplate.selectList("select_promotion_history", param);
    }

    /**
     * 获取该product参加的promoiton的总件数
     * @param param
     * @return
     */
    public int selectPromotionHistoryCnt(Map<String, Object> param) {
        return updateTemplate.selectOne("select_promotion_history_cnt", param);
    }
}
