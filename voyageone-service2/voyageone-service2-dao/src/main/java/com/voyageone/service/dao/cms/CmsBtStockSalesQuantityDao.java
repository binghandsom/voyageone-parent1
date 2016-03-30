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
public class CmsBtStockSalesQuantityDao extends BaseDao {

    public List<Map<String, Object>> selectStockSalesQuantity(Map<String, Object> param) {
        return selectList("select_stock_sales_quantity", param);
    }

    public Integer selectStockSalesQuantityQty(Map<String, Object> param) {
        return selectOne("select_stock_sales_quantity_qty", param);
    }

    public int updateStockSalesQuantity(Map<String, Object> param) {
        return update("update_stock_sales_quantity", param);
    }
}
