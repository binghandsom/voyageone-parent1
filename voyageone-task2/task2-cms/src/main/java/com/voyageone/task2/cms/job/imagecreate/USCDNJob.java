package com.voyageone.task2.cms.job.imagecreate;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.imagecreate.USCDNJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2016/4/26.
 */
@Service
public class USCDNJob extends BaseMQTaskJob {
    @Autowired
    USCDNJobService service;
    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
