package com.voyageone.task2.cms.usa.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 美国CMS2 Data amount Job
 *
 * @Author rex.wu
 * @Create 2017-07-11 16:14
 */
@Component("UsaCmsDataAmountJob")
public class UsaCmsDataAmountJob extends BaseTaskJob {

    @Autowired
    private UsaCmsDataAmountJobService usaCmsDataAmountJobService;

    @Override
    protected BaseTaskService getTaskService() {
        return usaCmsDataAmountJobService;
    }
}
