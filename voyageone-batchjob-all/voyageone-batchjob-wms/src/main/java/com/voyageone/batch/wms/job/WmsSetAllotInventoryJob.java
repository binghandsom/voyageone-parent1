package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSetAllotInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 物理库存分配
 * 根据物品的SKU来取得所属的仓库并进行相应分配
 *
 * @author Jack
 */
@Component("wmsSetAllotInventoryTask")
public class WmsSetAllotInventoryJob extends BaseTaskJob {

    @Autowired
    WmsSetAllotInventoryService wmsSetAllotInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSetAllotInventoryService;
    }

}
