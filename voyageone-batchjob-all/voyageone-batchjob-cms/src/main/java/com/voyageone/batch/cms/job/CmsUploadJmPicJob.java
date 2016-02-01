package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsUploadJmPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Component("CmsUploadJmPicJob")
public class CmsUploadJmPicJob extends BaseTaskJob {
    @Autowired
    private CmsUploadJmPicService cmsUploadJmPicService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsUploadJmPicService;
    }
}
