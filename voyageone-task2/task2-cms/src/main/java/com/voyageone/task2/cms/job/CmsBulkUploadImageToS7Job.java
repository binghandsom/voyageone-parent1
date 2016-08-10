package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsBulkUploadImageToS7Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CMS 批量上传图片文件到Scene Server Ftp JOB
 *    replace com.voyageone.task2.cms.service.monitor.ImageUploadService
 *
 * @author chuanyu.liang on 2016/07/18.
 * @version 2.1.0
 * @since 2.1.0
 */
@Component("CmsBulkUploadImageToS7Job")
public class CmsBulkUploadImageToS7Job extends BaseTaskJob {
    @Autowired
    CmsBulkUploadImageToS7Service cmsBulkUploadImageToS7Service;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsBulkUploadImageToS7Service;
    }
}
