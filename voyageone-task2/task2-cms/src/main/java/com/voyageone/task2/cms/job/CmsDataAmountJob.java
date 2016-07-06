package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsDataAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dell on 2016/7/5.
 */
@Component("CmsDataAmountService")
public class CmsDataAmountJob extends BaseTaskJob {

    @Autowired
    CmsDataAmountService cmsDataAmountService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsDataAmountService;
    }
}
