package com.voyageone.task2.vms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.vms.service.VmsPrcInvFileScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * vms价格和库存文件扫描job
 * Created by vantis on 16-9-8.
 */
@Component(value = "VmsPrcInvFileScanJob")
public class VmsPrcInvFileScanJob extends BaseTaskJob {

    private VmsPrcInvFileScanService vmsPrcInvFileScanService;

    @Autowired
    public VmsPrcInvFileScanJob (VmsPrcInvFileScanService vmsPrcInvFileScanService) {
        this. vmsPrcInvFileScanService = vmsPrcInvFileScanService;
    }

    @Override
    protected BaseTaskService getTaskService() {
        return vmsPrcInvFileScanService;
    }
}
