package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsPlatformProductImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 从天猫拉取商品数据, 插入到主数据 (用于旧系统迁移到新系统)
 *
 * @author james.li on 2015/12/10
 * @since. 2.0.0
 */
@Component("CmsPlatformProductImportJob")
public class CmsPlatformProductImportJob extends BaseTaskJob {

    @Autowired
    private CmsPlatformProductImportService cmsPlatformProductImportService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsPlatformProductImportService;
    }
}
