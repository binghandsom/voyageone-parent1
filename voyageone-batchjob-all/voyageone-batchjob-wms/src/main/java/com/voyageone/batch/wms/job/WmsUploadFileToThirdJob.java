package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsUploadFileToThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dell on 2015/8/20.
 */
@Component("wmsUploadFileToThirdJobTask")
public class WmsUploadFileToThirdJob extends BaseTaskJob {
    @Autowired
    WmsUploadFileToThirdService wmsUploadFileToThirdService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsUploadFileToThirdService;
    }
}
