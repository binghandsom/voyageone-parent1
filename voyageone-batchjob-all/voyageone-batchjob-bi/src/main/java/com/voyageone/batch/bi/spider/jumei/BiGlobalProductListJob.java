package com.voyageone.batch.bi.spider.jumei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.jumei.task.BiGlobalProductListTask;


@Component("biGlobalProductListJob")
public class BiGlobalProductListJob extends BaseTaskJob {

    @Autowired
    BiGlobalProductListTask globalProductListTask;

    @Override
    protected BaseTaskService getTaskService() {
        return globalProductListTask;
    }

}
