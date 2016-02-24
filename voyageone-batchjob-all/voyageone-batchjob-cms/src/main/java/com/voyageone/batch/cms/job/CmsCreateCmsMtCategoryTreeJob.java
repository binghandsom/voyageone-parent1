package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.CmsCreateCmsMtCategoryTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/2/22.
 * @version 2.0.0
 */
@Component("CmsCreateCmsMtCategoryTreeJob")
public class CmsCreateCmsMtCategoryTreeJob extends BaseTaskJob {
    @Autowired
    CmsCreateCmsMtCategoryTreeService cmsCreateCmsMtCategoryTreeService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsCreateCmsMtCategoryTreeService;
    }
}