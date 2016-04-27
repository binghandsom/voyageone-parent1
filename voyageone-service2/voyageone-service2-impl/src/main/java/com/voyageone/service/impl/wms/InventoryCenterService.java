package com.voyageone.service.impl.wms;

import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * InventoryCenterLogicService
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class InventoryCenterService extends BaseService {
    @Autowired
    private WmsBtInventoryCenterLogicDao inventoryCenterLogicDao;

    public List<WmsBtInventoryCenterLogicModel> getInventoryItemDetail(Map<String, Object> param) {
        return inventoryCenterLogicDao.selectItemDetail(param);
    }

    public Integer getLogicInventoryCnt(Map<String, Object> param) {
        return inventoryCenterLogicDao.selectLogicInventoryCnt(param);
    }
}
