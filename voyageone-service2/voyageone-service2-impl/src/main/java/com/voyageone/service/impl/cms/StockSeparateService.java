package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtStockSeparateItemDao;
import com.voyageone.service.dao.cms.CmsBtStockSeparatePlatformInfoDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Tag Service
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class StockSeparateService extends BaseService {

    @Autowired
    private CmsBtStockSeparateItemDao cmsBtStockSeparateItemDao;
    @Autowired
    private CmsBtStockSeparatePlatformInfoDao cmsBtStockSeparatePlatformInfoDao;

    public List<Map<String,Object>> getPlatformStockSeparateList(Map param){
        return cmsBtStockSeparatePlatformInfoDao.selectStockSeparatePlatform(param);
    }

    public List<Map<String, Object>> getStockSeparateItem(Map<String, Object> sqlParam){
        return cmsBtStockSeparateItemDao.selectStockSeparateItem(sqlParam);
    }

    public List<String>  getStockSeparateItemPageSku(Map<String, Object> sqlParam){
        return null;//cmsBtStockSeparateItemDao.selectStockSeparateItemPageSku(sqlParam);
    }

    public int getStockSeparateItemHistoryCnt(Map<String, Object> param){
        return cmsBtStockSeparateItemDao.selectStockSeparateItemHistoryCnt(param);
    }

}
