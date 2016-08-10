package com.voyageone.task2.vms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.vms.service.VmsFeedFileImportService;
import com.voyageone.task2.vms.service.VmsFeedFileScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 扫描Vendor通过ftp上传的Csv文件，并加入文件管理表（vms_bt_feed_file）
 * Created on 16/06/29.
 * @author jeff.duan
 * @version 1.0
 */
@Component("VmsFeedFileScanJob")
public class VmsFeedFileScanJob extends BaseTaskJob {

    @Autowired
    private VmsFeedFileScanService vmsFeedFileScanService;

    @Override
    protected BaseTaskService getTaskService() {
        return vmsFeedFileScanService;
    }
}

