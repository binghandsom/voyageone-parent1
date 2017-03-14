package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsImportCategoryTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将主类目/对应的平台类目信息导入到cms_mt_category_tree_all表
 *
 * @author jeff.duan on 2016/6/12.
 * @version 2.1.0
 */
@Component("CmsImportCategoryTreeJob")
public class CmsImportCategoryTreeJob extends BaseTaskJob {

    @Autowired
    private CmsImportCategoryTreeService cmsImportCategoryTreeService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsImportCategoryTreeService;
    }
}

