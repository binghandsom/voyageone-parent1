package com.voyageone.task2.cms.job.imagecreate;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.imagecreate.AliYunOSSJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliYunOSSJob extends BaseMQTaskJob {
@Autowired
    AliYunOSSJobService service;
    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
