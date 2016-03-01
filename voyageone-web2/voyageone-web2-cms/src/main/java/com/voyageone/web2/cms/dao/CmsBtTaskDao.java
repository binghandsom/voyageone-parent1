package com.voyageone.web2.cms.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.cms.model.CmsBtTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jonasvlag on 16/2/29.
 * @version 2.0.0
 */
@Repository
public class CmsBtTaskDao extends WebBaseDao {

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public int insert(CmsBtTaskModel model) {
        return insert("cms_bt_tasks_insert", model);
    }

    /**
     * 更新数据, 只更新 Config 和 Name
     */
    public int update(CmsBtTaskModel model) {
        return update("cms_bt_tasks_update", model);
    }

    public CmsBtTaskModel selectById(int task_id) {
        return selectOne("cms_bt_tasks_selectById", parameters("task_id", task_id));
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
