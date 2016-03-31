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
public class CmsBtStockSeparatePlatformInfoDao extends BaseDao {
    public List<Map<String, Object>> selectStockSeparatePlatform(Map<String, Object> param) {
        return selectList("select_stock_separate_platform", param);
    }

    public int updateStockSeparatePlatform(Map<String, Object> param) {
        return update("update_stock_separate_platform", param);
    }

    public int deleteStockSeparatePlatform(Map<String, Object> param) {
        return delete("delete_stock_separate_platform", param);
    }
    public String selectStockSeparatePlatFormInfoById(String cart_id,String resvert_time){
        return selectOne("select_cms_bt_stock_separate_platform_info", parameters(
                "cart_id", cart_id,
                "resvert_time", resvert_time
        ));
    }
    public int insert(Map stockSeparatePlatFormInfoMap) {
        return insert("insert_cms_cms_bt_stock_separate_platform_info", stockSeparatePlatFormInfoMap);
    }
    public List<Map<String,Object>> stockSeparatePlatFormInfoMapByTaskID(String task_id){
        return selectList("select_cms_bt_stock_separate_platform_info_by_task_id",task_id);
    }
}
