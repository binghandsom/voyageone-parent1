package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSetClientInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 第三方渠道库存计算
 * 库存由第三方渠道自己管理时，定时计算推送过来的库存
 *
 * @author Jack
 */
@Component("wmsSetClientInventoryTask")
public class WmsSetClientInventoryJob extends BaseTaskJob {

    @Autowired
    WmsSetClientInventoryService wmsSetClientInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSetClientInventoryService;
    }

}
