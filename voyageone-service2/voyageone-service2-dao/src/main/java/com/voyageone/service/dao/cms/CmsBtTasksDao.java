package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jonasvlag on 16/2/29.
 * @version 2.0.0
 */
@Repository
public class CmsBtTasksDao extends ServiceBaseDao {

    public int insert(CmsBtTasksModel model) {
        return insert("cms_bt_tasks_insert", model);
    }

    /**
     * 更新数据, 只更新 Config 和 Name
     */
    public int update(CmsBtTasksModel model) {
        return update("cms_bt_tasks_update", model);
    }

    public CmsBtTasksModel selectByIdWithPromotion(int task_id) {
        return selectOne("cms_bt_tasks_select", parameters("task_id", task_id));
    }

    public List<CmsBtTasksModel> selectByName(int promotion_id, String task_name, String channelId, int task_type) {
        return selectList("cms_bt_tasks_select", parameters(
                "promotion_id", promotion_id,
                "channelId", channelId,
                "task_name", task_name,
                "task_type", task_type + ""));
    }

    public List<CmsBtTasksModel> selectTaskWithPromotionByChannel(Map<String,Object> searchInfo) {
        return selectList("cms_bt_tasks_select", searchInfo);
    }

    public int delete(CmsBtTasksModel mode) {
        return delete("cms_bt_tasks_delete", mode);
    }

    public String selectCmsBtTaskByTaskName(String task_name){
        return selectOne("select_cms_bt_tasks_task_id",task_name);
    }
}
