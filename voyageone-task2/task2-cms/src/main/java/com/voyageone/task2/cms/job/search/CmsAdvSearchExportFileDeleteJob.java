package com.voyageone.task2.cms.job.search;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.search.CmsAdvSearchExportFileDeleteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rex.wu on 2016/12/12.
 * 高级检索导出文件，定时删除文件
 */
@Component("cmsAdvSearchExportFileDeleteJob")
public class CmsAdvSearchExportFileDeleteJob extends BaseTaskJob {

    @Autowired
    private CmsAdvSearchExportFileDeleteService cmsAdvSearchExportFileDeleteService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsAdvSearchExportFileDeleteService;
    }
}
