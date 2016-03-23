package com.voyageone.task2.cms.job.feed.analysis;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.feed.BcbgAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * BCBG Feed 数据分析导入 CMS 的定时任务
 * Created by Jonas on 10/23/15.
 */
@Component("BcbgAnalysisJob")
public class BcbgAnalysisJob extends BaseTaskJob {

    @Autowired
    private BcbgAnalysisService analysisService;

    @Override
    protected BaseTaskService getTaskService() {
        return analysisService;
    }
}
