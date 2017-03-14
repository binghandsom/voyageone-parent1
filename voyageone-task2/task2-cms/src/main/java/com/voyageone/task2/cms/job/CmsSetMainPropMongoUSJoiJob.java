package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsSetMainPropMongoUSJoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将产品导入到主数据的值表
 *
 * @author tom
 */
@Component("CmsSetMainPropMongoUSJoiJob")
public class CmsSetMainPropMongoUSJoiJob extends BaseTaskJob {

    @Autowired
    private CmsSetMainPropMongoUSJoiService cmsSetMainPropMongoService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSetMainPropMongoService;
    }
}

