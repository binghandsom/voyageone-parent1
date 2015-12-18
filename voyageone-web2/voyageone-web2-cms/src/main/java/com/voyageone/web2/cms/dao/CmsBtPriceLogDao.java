package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtPriceLogModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Repository
public class CmsBtPriceLogDao extends BaseDao {
    public List<CmsBtPriceLogModel> selectPriceLogByCode(String code) {
        return updateTemplate.selectList("select_price_log_by_code", code);
    }

    public List<CmsBtPriceLogModel> selectPriceLogBySku(String sku) {
        return updateTemplate.selectList("select_price_log_by_sku", sku);
    }
}
