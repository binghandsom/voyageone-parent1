package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsGetJmMasterService;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Component("CmsGetJmMasterJob")
public class CmsGetJmMasterJob extends BaseTaskJob {
    private CmsGetJmMasterService cmsGetJmMasterService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsGetJmMasterService;
    }
}
