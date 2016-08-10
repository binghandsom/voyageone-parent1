package com.voyageone.task2.vms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.vms.service.VmsFeedFileScanService;
import com.voyageone.task2.vms.service.VmsMakeFinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成财务报表
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Component("VmsMakeFinancialReportJob")
public class VmsMakeFinancialReportJob extends BaseTaskJob {

    @Autowired
    private VmsMakeFinancialReportService vmsMakeFinancialReportService;

    @Override
    protected BaseTaskService getTaskService() {
        return vmsMakeFinancialReportService;
    }
}

