package com.voyageone.task2.vms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.vms.service.VmsGetHomeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取得Home页面的显示信息
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Component("VmsGetHomeInfoJob")
public class VmsGetHomeInfoJob extends BaseTaskJob {

    @Autowired
    private VmsGetHomeInfoService vmsGetHomeInfoService;

    @Override
    protected BaseTaskService getTaskService() {
        return vmsGetHomeInfoService;
    }
}

