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
public class CmsBtStockSeparateIncrementTaskDao extends BaseDao {

    public List<Map<String, Object>> selectStockSeparateIncrementTask(Map<String, Object> param) {
        return selectList("select_stock_separate_increment_task", param);
    }

    public int deleteStockSeparateIncrementTask(Map<String, Object> param) {
        return delete("delete_stock_separate_increment_task", param);
    }
}
