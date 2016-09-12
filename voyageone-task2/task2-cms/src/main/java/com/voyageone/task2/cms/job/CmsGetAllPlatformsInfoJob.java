package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.GetAllPlatformsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取所有平台tree和schema
 *
 * @author morse on 2016/9/12
 * @version 2.6.0
 */
@Component("CmsGetAllPlatformsInfoJob")
public class CmsGetAllPlatformsInfoJob extends BaseTaskJob {

    @Autowired
    GetAllPlatformsInfoService getAllPlatformsInfoService;

    @Override
    protected BaseTaskService getTaskService() {
        return getAllPlatformsInfoService;
    }

}
