package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
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
        return selectList("cms_bt_stock_separate_item_selectStockSeparateItem", param);
    }

    public List<Map<String, Object>> selectStockSeparateItemBySqlMap(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_item_selectStockSeparateItemBySqlMap", param);
    }

    public List<Object> selectStockSeparateItemBySqlObject(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_item_selectStockSeparateItemBySqlObject", param);
    }

    public Integer selectStockSeparateSuccessQty(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_item_selectStockSeparateSuccessQty", param);
    }

    public Integer selectStockSeparateItemByStatus(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_item_selectStockSeparateItemByStatus", param);
    }

    public int selectStockSeparateItemHistoryCnt(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_item_selectStockSeparateItemHistoryCnt", param);
    }

    public int updateStockSeparateItem(Map<String, Object> param) {
        return update("cms_bt_stock_separate_item_updateStockSeparateItem", param);
    }

    public int deleteStockSeparateItem(Map<String, Object> param) {
        return delete("cms_bt_stock_separate_item_deleteStockSeparateItem", param);
    }

    public int insertStockSeparateItem(Map<String, Object> param) {
        return insert("cms_bt_stock_separate_insertStockSeparateItem", param);
    }

    public List<Map<String, Object>> selectStockSeparateDetailAll(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_selectStockSeparateDetailAll", param);
    }

    public List<StockExcelBean> selectExcelStockInfo(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_selectExcelStockInfo", param);
    }

    public int insertStockSeparateItemByList(List<Map<String, Object>> param) {
        return insert("cms_bt_stock_separate_insertStockSeparateItemByList", param);
    }

    public List<Map<String, Object>> selectExcelStockErrorInfo(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_selectExcelStockErrorInfo", param);
    }

}
