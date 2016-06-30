package com.voyageone.task2.vms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.vms.service.VmsFeedFileImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 将Feed信息导入FeedInfo表
 *
 * @author jeff.duan
 */
@Component("VmsFeedFileImportJob")
public class VmsFeedFileImportJob extends BaseTaskJob {

    @Autowired
    private VmsFeedFileImportService vmsFeedImportService;

    @Override
    protected BaseTaskService getTaskService() {
        return vmsFeedImportService;
    }
}

