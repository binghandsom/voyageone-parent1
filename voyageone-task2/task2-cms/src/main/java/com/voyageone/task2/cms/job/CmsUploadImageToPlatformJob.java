package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsImagePostScene7Service;
import com.voyageone.task2.cms.service.CmsUploadImageToPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 图片上传到平台（暂时只支持淘宝/天猫/天猫国际和聚美）
 *
 * @author james
 */
@Component("CmsUploadImageToPlatformJob")
public class CmsUploadImageToPlatformJob extends BaseTaskJob {

    @Autowired
    CmsUploadImageToPlatformService cmsUploadImageToPlatformService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsUploadImageToPlatformService;
    }

}
