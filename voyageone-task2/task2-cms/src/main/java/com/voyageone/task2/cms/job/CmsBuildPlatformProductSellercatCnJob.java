package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBuildPlatformProductSellercatCnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 独立域名类目-产品关系(排序)推送Job
 *
 * @author morse.lu on 2016/09/23.
 * @version 2.6.0
 * @since 2.6.0
 */
@Component("CmsBuildPlatformProductSellercatCnJob")
public class CmsBuildPlatformProductSellercatCnJob extends BaseTaskJob {

    @Autowired
    private CmsBuildPlatformProductSellercatCnService cmsBuildPlatformProductSellercatCnService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBuildPlatformProductSellercatCnService;
    }

}

