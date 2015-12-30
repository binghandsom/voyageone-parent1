package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.GetPlatformCategorySchemaService;
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
