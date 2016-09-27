package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformImageUploadCnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 独立域名图片上传Job
 *
 * @author morse.lu on 2016/09/27.
 * @version 2.6.0
 * @since 2.6.0
 */
@Component("CmsBuildPlatformImageUploadCnJob")
public class CmsBuildPlatformImageUploadCnJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformImageUploadCnService cmsBuildPlatformImageUploadCnService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformImageUploadCnService;
    }

}

