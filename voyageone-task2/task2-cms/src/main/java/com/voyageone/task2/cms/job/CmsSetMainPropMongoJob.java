package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsSetMainPropMongo2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将产品导入到主数据的值表
 *
 * @author tom
 */
@Component("CmsSetMainPropMongoJob")
public class CmsSetMainPropMongoJob extends BaseTaskJob {

    @Autowired
    private CmsSetMainPropMongo2Service cmsSetMainPropMongoService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSetMainPropMongoService;
    }
}

