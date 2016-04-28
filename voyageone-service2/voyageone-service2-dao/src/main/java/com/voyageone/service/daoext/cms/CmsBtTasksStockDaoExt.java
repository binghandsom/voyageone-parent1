package com.voyageone.service.daoext.cms;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 *
 * @version 2.0.0
 */
@Repository
public class CmsBtTasksStockDaoExt extends BaseDao {

//    public List<Map<String, Object>> selectStockSeparatePlatform(Map param) {
//        return selectList("select_stock_separate_platform", param);
//    }

    public List<Map<String, Object>> selectStockSeparatePlatform(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_platform_info_selectStockSeparatePlatform", param);
    }

    public int updateStockSeparatePlatform(Map<String, Object> param) {
        return update("cms_bt_stock_separate_platform_info_updateStockSeparatePlatform", param);
    }

    public int deleteStockSeparatePlatform(Map<String, Object> param) {
        return delete("cms_bt_stock_separate_platform_info_deleteStockSeparatePlatform", param);
    }

    public List<Map<String, Object>> selectStockSeparatePlatFormInfoById(int cartId, String revertTime, String channelId) {
        return selectList("cms_bt_stock_separate_platform_selectStockSeparatePlatFormInfoById", parameters(
                "cartId", cartId,
                "channelId", channelId,
                "revertTime", revertTime
        ));
    }

    public int insert(Map stockSeparatePlatFormInfoMap) {
        return insert("cms_bt_stock_separate_platform_info_insert", stockSeparatePlatFormInfoMap);
    }

    public List<Map<String, Object>> selectStockSeparatePlatFormInfoMapByTaskID(String task_id) {
        return selectList("cms_bt_stock_separate_platform_info_stockSeparatePlatFormInfoMapByTaskID", task_id);
    }
}
