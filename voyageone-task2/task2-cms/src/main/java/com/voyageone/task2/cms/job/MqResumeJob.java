package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.MqResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mq恢复任务
 *
 * @author aooer 2016/2/29.
 */

@Component("mqResumeJob")
public class MqResumeJob extends BaseTaskJob {

    @Autowired
    private MqResumeService mqResumeService;

    @Override
    protected BaseTaskService getTaskService() {
        return mqResumeService;
    }
}
