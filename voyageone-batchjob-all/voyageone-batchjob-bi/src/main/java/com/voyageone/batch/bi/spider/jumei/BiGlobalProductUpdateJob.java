package com.voyageone.batch.bi.spider.jumei;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.jumei.task.BiGlobalProductUpdateTask;


@Component("biGlobalProductUpdateJob")
public class BiGlobalProductUpdateJob extends BaseTaskJob {

    @Autowired
    BiGlobalProductUpdateTask globalProductUpdateTask;

    @Override
    protected BaseTaskService getTaskService() {
        return globalProductUpdateTask;
    }

}
