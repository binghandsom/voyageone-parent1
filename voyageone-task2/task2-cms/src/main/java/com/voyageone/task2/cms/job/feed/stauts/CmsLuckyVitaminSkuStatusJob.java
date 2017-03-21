package com.voyageone.task2.cms.job.feed.stauts;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.status.LuckyVitaminSkuStatusCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2017/3/21.
 */
@Component("CmsLuckyVitaminSkuStatusJob")
public class CmsLuckyVitaminSkuStatusJob extends BaseTaskJob {

    @Autowired
    LuckyVitaminSkuStatusCheckService luckyVitaminSkuStatusCheckService;
    @Override
    protected BaseTaskService getTaskService() {
        return luckyVitaminSkuStatusCheckService;
    }
}
