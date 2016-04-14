package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparateIncrementTaskDao extends BaseDao {

    public List<Map<String, Object>> selectStockSeparateIncrementTask(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_increment_task_selectStockSeparateIncrementTask", param);
    }

    public int deleteStockSeparateIncrementTask(Map<String, Object> param) {
        return delete("cms_bt_stock_separate_increment_task_deleteStockSeparateIncrementTask", param);
    }

    public int insertStockSeparateIncrementTask(Map<String, Object> param) {
        return insert("insert_cms_bt_stock_separate_increment_task_by_taskId", param);
    }

    public int updateStockSeparateIncrementTask(Map<String, Object> param) {
        return update("update_cms_bt_stock_separate_increment_task_by_subTaskId", param);
    }
}
