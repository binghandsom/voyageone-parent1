package com.voyageone.batch.wms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.wms.service.WmsReceiptConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sneakerhead on 2015/10/22.
 */
@Component("wmsReceiptConfirmationJob")
public class WmsReceiptConfirmationJob extends BaseTaskJob {

    @Autowired
    private WmsReceiptConfirmationService wmsReceiptConfirmationService;

    @Override
    protected BaseTaskService getTaskService() {
        return wmsReceiptConfirmationService;
    }
}
