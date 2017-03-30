package com.voyageone.task2.cms.job.feed.stauts;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.status.DfoSkuStatusCheckService;
import com.voyageone.task2.cms.service.feed.status.EdcSkincareStatusCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2017/3/17.
 */
@Component("CmsEdcStatusJob")
public class CmsEdcStatusJob extends BaseTaskJob {

    @Autowired
    EdcSkincareStatusCheckService edcSkincareStatusCheckService;
    @Override
    protected BaseTaskService getTaskService() {
        return edcSkincareStatusCheckService;
    }
}
