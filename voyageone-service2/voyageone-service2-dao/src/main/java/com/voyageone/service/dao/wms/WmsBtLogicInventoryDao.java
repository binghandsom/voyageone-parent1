package com.voyageone.service.dao.wms;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class WmsBtLogicInventoryDao extends BaseDao {
    public Integer selectLogicInventoryCnt(Map<String, Object> param) {
        return selectOne("select_logic_inventory_cnt", param);
    }
}
