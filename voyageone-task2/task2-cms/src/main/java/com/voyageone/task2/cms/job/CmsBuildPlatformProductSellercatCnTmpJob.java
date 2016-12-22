package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductSellercatCnTmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 独立域名类目-产品关系(排序)推送Job
 *
 * @author morse.lu on 2016/09/23.
 * @version 2.6.0
 * @since 2.6.0
 */
@Component("CmsBuildPlatformProductSellercatCnTmpJob")
public class CmsBuildPlatformProductSellercatCnTmpJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductSellercatCnTmpService cmsBuildPlatformProductSellercatCnTmpService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductSellercatCnTmpService;
    }

}

