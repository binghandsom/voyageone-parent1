package com.voyageone.task2.cms.job.feed.stauts;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.status.DfoSkuStatusCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2017/3/17.
 */
@Component("CmsDfoStatusJob")
public class CmsDfoStatusJob extends BaseTaskJob {

    @Autowired
    DfoSkuStatusCheckService dfoSkuStatusCheckService;
    @Override
    protected BaseTaskService getTaskService() {
        return dfoSkuStatusCheckService;
    }
}
