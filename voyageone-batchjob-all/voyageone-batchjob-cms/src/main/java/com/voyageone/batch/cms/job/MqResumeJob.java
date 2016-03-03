package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.feed.BcbgAnalysisService;
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
    private BcbgAnalysisService analysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return analysisService;
    }
}
