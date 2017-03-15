package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.OverStockTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2017/3/13.
 */
@Component("OverStockTempJob")
public class OverStockTempJob   extends BaseTaskJob {

    @Autowired
    OverStockTempService overStockTempService;
    @Override
    protected BaseTaskService getTaskService() {
        return overStockTempService;
    }
}
