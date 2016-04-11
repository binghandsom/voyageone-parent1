package com.voyageone.task2.cms.job.jumei;

import com.voyageone.task2.base.BaseMQTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.jumei.JmBtPromotionExportService;
import com.voyageone.task2.cms.service.jumei.JmBtPromotionImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
public class JmBtPromotionExportJob extends BaseMQTaskJob {

    @Autowired
    private JmBtPromotionExportService service;

    @Override
    protected BaseTaskService getTaskService() {
        return service;
    }
}
