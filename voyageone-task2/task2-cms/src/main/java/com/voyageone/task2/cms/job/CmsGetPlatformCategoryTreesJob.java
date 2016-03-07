package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.GetPlatformCategoryTreesService;
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
