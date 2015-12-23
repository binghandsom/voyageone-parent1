package com.voyageone.web2.cms.views.pop.price;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtPriceLogDao;
import com.voyageone.web2.cms.model.CmsBtPriceLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Service
public class CmsPriceHistoryService extends BaseAppService {

    @Autowired
    private CmsBtPriceLogDao cmsBtPriceLogDao;

    /**
     * @param params 检索条件
     * @return 价格修改记录列表
     */
    public Map<String, Object> getPriceHistory(Map<String, Object> params) {
        int count;
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPriceLogModel> priceList;
        boolean flag = (boolean) params.get("flag");
        if (flag) {
            String code = (String) params.get("code");
            priceList = cmsBtPriceLogDao.selectPriceLogByCode(params);
            count = cmsBtPriceLogDao.selectPriceLogByCodeCnt(params);
        } else {
            String sku = (String) params.get("sku");
            priceList = cmsBtPriceLogDao.selectPriceLogBySku(params);
            count = cmsBtPriceLogDao.selectPriceLogBySkuCnt(params);
        }
        result.put("list", priceList);
        result.put("total", count);
        return result;
    }
}
