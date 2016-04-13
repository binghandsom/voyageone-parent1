package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.platform.uj.UploadToUSJoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/4/11.
 * @version 2.0.0
 */
@Component("CmsUploadProductToUSJoiJob")
public class CmsUploadProductToUSJoiJob extends BaseTaskJob {

    @Autowired
    private UploadToUSJoiService uploadToUSJoiService;

    @Override
    protected BaseTaskService getTaskService() {
        return uploadToUSJoiService;
    }
}
