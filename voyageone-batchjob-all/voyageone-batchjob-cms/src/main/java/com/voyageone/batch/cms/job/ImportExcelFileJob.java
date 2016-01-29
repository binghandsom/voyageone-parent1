package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.AttributeTranslateService;
import com.voyageone.batch.cms.service.ImportExcelFileService;
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
