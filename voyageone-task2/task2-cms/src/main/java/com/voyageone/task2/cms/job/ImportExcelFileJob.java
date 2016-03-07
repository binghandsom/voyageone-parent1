package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.ImportExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jerry on 2016/01/23.
 */
@Component("ImportExcelFileJob")
public class ImportExcelFileJob extends BaseTaskJob {
    @Autowired
    private ImportExcelFileService importExcelFileService;

    @Override
    protected BaseTaskService getTaskService() {
        return importExcelFileService;
    }
}
