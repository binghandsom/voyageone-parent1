package com.voyageone.batch.cms.job;

import com.voyageone.batch.Context;
import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.PlatformCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 第三方平台类目和属性设定
 *
 * @author lewis
 */
@Component("platformCategoryTask")
public class CmsGetPlatformCategoryJob extends BaseTaskJob {

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return platformCategoryService;
    }

}
