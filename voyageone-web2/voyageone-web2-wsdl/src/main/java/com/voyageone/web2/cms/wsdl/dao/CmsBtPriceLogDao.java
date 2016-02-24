package com.voyageone.web2.cms.wsdl.dao;

import com.voyageone.web2.sdk.api.domain.CmsBtPriceLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Repository
public class CmsBtPriceLogDao extends WsdlBaseDao {

    public List<CmsBtPriceLogModel> selectPriceLogByCode(Map<String, Object> param) {
        return updateTemplate.selectList("select_price_log_by_code", param);
    }

    public int selectPriceLogByCodeCnt(Map<String, Object> param) {
        return updateTemplate.selectOne("select_price_log_by_code_cnt", param);
    }

    public List<CmsBtPriceLogModel> selectPriceLogBySku(Map<String, Object> param) {
        return updateTemplate.selectList("select_price_log_by_sku", param);
    }

    public int selectPriceLogBySkuCnt(Map<String, Object> param) {
        return updateTemplate.selectOne("select_price_log_by_sku_cnt", param);
    }

    public int insertCmsBtPriceLog(CmsBtPriceLogModel param) {
        return updateTemplate.insert("insert_cms_bt_price_log", param);
    }

    public int insertCmsBtPriceLogList(List<CmsBtPriceLogModel> paramList) {
        Map<String, List<CmsBtPriceLogModel>> insertDataMap = new HashMap<>();
        insertDataMap.put("list", paramList);
        return updateTemplate.insert("insert_cms_bt_price_log_list", insertDataMap);
    }
}
