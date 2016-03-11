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
public class CmsBtStockSeparateIncrementItemDao extends BaseDao {

    public int selectStockSeparateIncrementSuccessQty(Map<String, Object> param) {
        return selectOne("select_stock_separate_increment_success_qty", param);
    }

    public int selectStockSeparateIncrementSuccessQtyByTask(Map<String, Object> param) {
        return selectOne("select_stock_separate_increment_success_qty_by_task", param);
    }
}
