package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.cms.CmsBtTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jonasvlag on 16/2/29.
 * @version 2.0.0
 */
//Repository
public class CmsBtTaskDao extends BaseDao {

    public int insert(CmsBtTaskModel model) {
        return insert("cms_bt_tasks_insert", model);
    }

    /**
     * 更新数据, 只更新 Config 和 Name
     */
    public int update(CmsBtTaskModel model) {
        return update("cms_bt_tasks_update", model);
    }

    public CmsBtTaskModel selectByIdWithPromotion(int task_id) {
        return selectOne("cms_bt_tasks_selectByIdWithPromotion", parameters("task_id", task_id));
    }

    public List<CmsBtTaskModel> selectByName(int promotion_id, String task_name, int task_type) {
        return selectList("cms_bt_tasks_selectByName", parameters(
                "promotion_id", promotion_id,
                "task_name", task_name,
                "task_type", task_type));
    }

    public List<CmsBtTaskModel> selectTaskWithPromotionByChannel(String channel_id) {
        return selectList("cms_bt_tasks_selectTaskWithPromotionByChannel", parameters("channel_id", channel_id));
    }
}
