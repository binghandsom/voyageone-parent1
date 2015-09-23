package com.voyageone.batch.bi.spider.jumei;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.bi.spider.jumei.task.BiGlobalProductUploadTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("biGlobalProductUploadJob")
public class BiGlobalProductUploadJob extends BaseTaskJob {

    @Autowired
    BiGlobalProductUploadTask globalProductUploadTask;

    @Override
    protected BaseTaskService getTaskService() {
        return globalProductUploadTask;
    }

}
