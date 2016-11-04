package com.voyageone.service.impl.wms;

import com.voyageone.service.dao.wms.WmsBtInventoryCenterDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * InventoryCenterService
 *
 * @author jeff.duan 16/10/20
 * @version 2.0.0
 */
@Service
public class InventoryCenterService extends BaseService {
    @Autowired
    private WmsBtInventoryCenterDao inventoryCenterDao;

    public void updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        inventoryCenterDao.updateCodeForMove(channelId, itemCodeOld, skuList, itemCodeNew, modifier);
    }
}
