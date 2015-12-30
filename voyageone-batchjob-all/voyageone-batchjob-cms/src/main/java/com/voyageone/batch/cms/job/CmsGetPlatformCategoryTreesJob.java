package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.GetPlatformCategorySchemaService;
import com.voyageone.batch.cms.service.GetPlatformCategoryTreesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 第三方平台类目和属性设定
 *
 * @author lewis
 */
@Component("getPlatformCategoryTreesTask")
public class CmsGetPlatformCategoryTreesJob extends BaseTaskJob {

    @Autowired
    GetPlatformCategoryTreesService getPlatformCategoryTreesService;

    @Override
    protected BaseTaskService getTaskService() {
        return getPlatformCategoryTreesService;
    }

}
