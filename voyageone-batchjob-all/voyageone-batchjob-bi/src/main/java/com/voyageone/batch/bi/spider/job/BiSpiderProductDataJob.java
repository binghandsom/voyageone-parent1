package com.voyageone.batch.bi.spider.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.job.task.ProductDataService;

@Component("biSpiderProductDataJob")
public class BiSpiderProductDataJob extends BaseTaskJob {

    @Autowired
    ProductDataService productDataService;

    @Override
    protected BaseTaskService getTaskService() {
        return productDataService;
    }

}

