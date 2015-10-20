package com.voyageone.batch.core.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.service.HistoryDataTansferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by eric on 2015/10/14.
 * 把历史数据导入备份表Job
 */
@Component("historyDataTransferTask")
public class HistoryDataTransferJob extends BaseTaskJob {
    @Autowired
    private HistoryDataTansferService historyDataTansferService;

    @Override
    protected BaseTaskService getTaskService() {
        return historyDataTansferService;
    }
}
