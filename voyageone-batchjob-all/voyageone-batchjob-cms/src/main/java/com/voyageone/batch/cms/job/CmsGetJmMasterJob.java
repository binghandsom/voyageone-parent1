package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsGetJmMasterService;
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
