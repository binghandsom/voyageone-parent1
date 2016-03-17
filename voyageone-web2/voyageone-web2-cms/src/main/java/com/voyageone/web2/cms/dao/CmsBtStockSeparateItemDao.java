package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparateItemDao extends BaseDao {
    public List<Map<String, Object>> selectStockSeparateItem(Map<String, Object> param) {
        return selectList("select_stock_separate_item", param);
    }

    public List<Map<String, Object>> selectStockSeparateItemBySqlMap(Map<String, Object> param) {
        return selectList("select_stock_separate_item_by_sql_map", param);
    }

    public List<Object> selectStockSeparateItemBySqlObject(Map<String, Object> param) {
        return selectList("select_stock_separate_item_by_sql_object", param);
    }

    public int selectStockSeparateItemCnt(Map<String, Object> param) {
        return selectOne("select_stock_separate_item_cnt", param);
    }

    public Object selectStockSeparateSuccessQty(Map<String, Object> param) {
        return selectOne("select_stock_separate_success_qty", param);
    }

    public int selectStockSeparateItemHistoryCnt(Map<String, Object> param) {
        return selectOne("select_stock_separate_item_history_cnt", param);
    }

    public int updateStockSeparateItem(Map<String, Object> param) {
        return update("update_stock_separate_item", param);
    }

    public int deleteStockSeparateItem(Map<String, Object> param) {
        return delete("delete_stock_separate_item", param);
    }

    public List<Map<String, Object>> selectExcelStockInfo(Map<String, Object> param) {
        return selectList("select_stock_separate_item_excel_map", param);
    }
}
