package com.voyageone.service.impl.wms;

import com.voyageone.service.dao.wms.WmsBtInventoryCenterLogicDao;
import com.voyageone.service.dao.wms.WmsBtInventoryCenterOutputTmpDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private WmsBtInventoryCenterOutputTmpDao inventoryCenterOutputTmpDao;
}
