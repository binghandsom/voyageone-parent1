package com.voyageone.service.daoext.cms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 *
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparateIncrementItemDaoExt extends BaseDao {

    public List<Map<String, Object>> selectStockSeparateIncrement(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_increment_item_selectStockSeparateIncrement", param);
    }

    public Integer selectStockSeparateIncrementSuccessQty(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_increment_item_selectStockSeparateIncrementSuccessQty", param);
    }

    public Integer selectStockSeparateIncrementItemByStatus(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_increment_item_selectStockSeparateIncrementItemByStatus", param);
    }

    public int selectStockSeparateIncrementItemHistoryCnt(Map<String, Object> param) {
        return selectOne("cms_bt_stock_separate_increment_item_selectStockSeparateIncrementItemHistoryCnt", param);
    }

    public List<Map<String, Object>> selectStockSeparateIncrementItemBySql(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_increment_item_selectStockSeparateIncrementItemBySql", param);
    }

    public int updateStockSeparateIncrementItem(Map<String, Object> param) {
        return update("cms_bt_stock_separate_increment_item_updateStockSeparateIncrementItem", param);
    }

    public int deleteStockSeparateIncrementItem(Map<String, Object> param) {
        return delete("cms_bt_stock_separate_increment_item_deleteStockSeparateIncrementItem", param);
    }

    public List<StockIncrementExcelBean> selectExcelStockIncrementInfo(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_increment_item_selectExcelStockIncrementInfo", param);
    }

    public int insertStockSeparateIncrementItemByList(List<Map<String, Object>> param) {
        return insert("cms_bt_stock_separate_increment_item_insertStockSeparateIncrementItemByList", param);
    }
}
