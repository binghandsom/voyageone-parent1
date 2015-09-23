package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsSetLogicInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 逻辑库存计算
 * 按渠道计算出中心物理库存后，排除Ｏｐｅｎ、Ｒｅｓｅｒｖｅｄ、ＯｎＨｏｌｄ?的物品，将计算结果设定到中心逻辑库存
 *
 * @author Jack
 */
@Component("wmsSetLogicInventoryTask")
public class WmsSetLogicInventoryJob  extends BaseTaskJob {

    @Autowired
    WmsSetLogicInventoryService wmsSetLogicInventoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsSetLogicInventoryService;
    }
}
