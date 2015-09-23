package com.voyageone.batch.bi.spider.jumei;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.jumei.task.BiGlobalDealUploadTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("biGlobalDealUploadJob")
public class BiGlobalDealUploadJob extends BaseTaskJob {

    @Autowired
    BiGlobalDealUploadTask globalDealUploadTask;

    @Override
    protected BaseTaskService getTaskService() {
        return globalDealUploadTask;
    }

}
