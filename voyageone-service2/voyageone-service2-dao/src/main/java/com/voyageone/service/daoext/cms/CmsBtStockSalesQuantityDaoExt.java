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
public class CmsBtStockSalesQuantityDaoExt extends BaseDao {

    public List<Map<String, Object>> selectStockSalesQuantity(Map<String, Object> param) {
        return selectList("cms_bt_stock_sales_quantity_selectStockSalesQuantity", param);
    }

    public Integer selectStockSalesQuantityQty(Map<String, Object> param) {
        return selectOne("cms_bt_stock_sales_quantity_selectStockSalesQuantityQty", param);
    }

    public int updateStockSalesQuantity(Map<String, Object> param) {
        return update("cms_bt_stock_sales_quantity_updateStockSalesQuantity", param);
    }
}
