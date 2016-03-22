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
public class CmsBtStockSeparatePlatformInfoDao extends BaseDao {
    public List<Map<String, Object>> selectStockSeparatePlatform(Map<String, Object> param) {
        return selectList("select_stock_separate_platform", param);
    }

    public int deleteStockSeparatePlatform(Map<String, Object> param) {
        return delete("delete_stock_separate_platform", param);
    }
}
