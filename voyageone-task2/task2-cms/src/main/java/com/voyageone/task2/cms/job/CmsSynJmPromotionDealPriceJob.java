package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsSynJmPromotionDealPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2016/10/19.
 */
@Component("CmsSynJmPromotionDealPriceJob")
public class CmsSynJmPromotionDealPriceJob  extends BaseTaskJob {

    @Autowired
    private CmsSynJmPromotionDealPriceService cmsSynJmPromotionDealPriceService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsSynJmPromotionDealPriceService;
    }
}
