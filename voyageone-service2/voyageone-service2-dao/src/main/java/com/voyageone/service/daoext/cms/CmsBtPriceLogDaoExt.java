package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Repository
public class CmsBtPriceLogDaoExt extends ServiceBaseDao {

    public List<CmsBtPriceLogModel> selectPriceLogByCode(Map<String, Object> param) {
        return selectList("select_cms_bt_price_log_by_code", param);
    }

    public int selectPriceLogByCodeCnt(Map<String, Object> param) {
        return selectOne("select_cms_bt_price_log_by_code_cnt", param);
    }
    public int insertCmsBtPriceLogList(List<CmsBtPriceLogModel> paramList) {
        Map<String, List<CmsBtPriceLogModel>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", paramList);
        return insert("insert_cms_bt_price_log_list", insertDataMap);
    }
}
