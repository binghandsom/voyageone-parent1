package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSimTransferInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 模拟入出库
 * 根据入库，出库来进行对应库存增减
 *
 * @author Jack
 */
@Component("wmsSimTransferInfoTask")
public class WmsSimTransferInfoJob   extends BaseTaskJob {

    @Autowired
    WmsSimTransferInfoService wmsSimTransferInfoService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSimTransferInfoService;
    }

}
