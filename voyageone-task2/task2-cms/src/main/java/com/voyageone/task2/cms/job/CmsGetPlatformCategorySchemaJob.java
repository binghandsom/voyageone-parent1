package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.GetPlatformCategorySchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 第三方平台类目和属性设定
 *
 * @author lewis
 */
@Component("getPlatformCategorySchemaTask")
public class CmsGetPlatformCategorySchemaJob extends BaseTaskJob {

    @Autowired
    GetPlatformCategorySchemaService getPlatformCategorySchemaService;

    @Override
    protected BaseTaskService getTaskService() {
        return getPlatformCategorySchemaService;
    }

}
