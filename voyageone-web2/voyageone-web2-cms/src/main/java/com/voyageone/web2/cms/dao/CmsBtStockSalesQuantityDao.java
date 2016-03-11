package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSalesQuantityDao extends BaseDao {

    public Object selectStockSalesQuantity(Map<String, Object> param) {
        return selectOne("select_stock_sales_quantity", param);
    }
}
