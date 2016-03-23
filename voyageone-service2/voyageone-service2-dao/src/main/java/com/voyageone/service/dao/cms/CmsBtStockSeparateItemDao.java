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
public class CmsBtStockSeparateItemDao extends BaseDao {
    public List<Map<String, Object>> selectStockSeparateItem(Map<String, Object> param) {
        return selectList("select_stock_separate_item", param);
    }

    public List<String> selectStockSeparateItemPageSku(Map<String, Object> param) {
        return selectList("select_stock_separate_item_page_sku", param);
    }

    public int selectStockSeparateItemCnt(Map<String, Object> param) {
        return selectOne("select_stock_separate_item_cnt", param);
    }

    public int selectStockSeparateItemHistoryCnt(Map<String, Object> param) {
        return selectOne("select_stock_separate_item_history_cnt", param);
    }
}
