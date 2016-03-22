package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparateIncrementItemDao extends BaseDao {

    public int selectStockSeparateIncrementItemCnt(Map<String, Object> param) {
        return selectOne("select_stock_separate_increment_item_cnt", param);
    }
}
