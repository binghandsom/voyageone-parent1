package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsInvHisCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 一月一次存储库存情况，生成库存报表的时候以此表做基础计算库存
 * @author sky
 * @created 20150715
 */
@Component("wmsInvHisCalcJobTask")
public class WmsInvHisCalcJob extends BaseTaskJob{

    @Autowired
    WmsInvHisCalcService wmsInvHisCalcService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsInvHisCalcService;
    }
}
