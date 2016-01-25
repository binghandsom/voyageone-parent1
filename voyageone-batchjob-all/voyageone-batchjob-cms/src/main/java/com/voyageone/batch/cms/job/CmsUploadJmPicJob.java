package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Component("CmsUploadJmPicJob")
public class CmsUploadJmPicJob extends BaseTaskJob {
    @Override
    protected BaseTaskService getTaskService() {
        return null;
    }
}
