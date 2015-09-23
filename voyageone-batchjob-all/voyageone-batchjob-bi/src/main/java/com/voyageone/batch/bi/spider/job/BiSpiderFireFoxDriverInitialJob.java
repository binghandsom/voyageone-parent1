package com.voyageone.batch.bi.spider.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.job.task.FireFoxDriverInitialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("biSpiderFireFoxDriverInitialJob")
public class BiSpiderFireFoxDriverInitialJob extends BaseTaskJob {

    @Autowired
    FireFoxDriverInitialService fireFoxDriverInitialService;

    @Override
    protected BaseTaskService getTaskService() {
        return fireFoxDriverInitialService;
    }

}

