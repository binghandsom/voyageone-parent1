package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.bean.promotion.task.StockExcelBean;
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

    public Integer selectStockSeparateSuccessQty(Map<String, Object> param) {
        return selectOne("select_stock_separate_success_qty", param);
    }

    public Integer selectStockSeparateItemByStatus(Map<String, Object> param) {
        return selectOne("select_stock_separate_item_by_status", param);
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

    public int insertStockSeparateItem(Map<String, Object> param) {
        return insert("insert_cms_bt_stock_separate_item", param);
    }

    public List<Map<String, Object>> selectStockSeparateDetailAll(Map<String, Object> param) {
        return selectList("select_stock_separate_detail_all", param);
    }

    public List<StockExcelBean> selectExcelStockInfo(Map<String, Object> param) {
        return selectList("select_stock_separate_item_excel_map", param);
    }

    public int insertStockSeparateItemFromExcel(List<Map<String, Object>> param) {
        return insert("insert_cms_bt_stock_separate_item_excel", param);
    }

}
