package com.voyageone.batch.cms.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.AttributeTranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jacky on 2015/10/15.
 */
@Component("attributeTranslateJob")
public class AttributeTranslateJob extends BaseTaskJob {
    @Autowired
    private AttributeTranslateService attributeTranslateService;

    @Override
    protected BaseTaskService getTaskService() {
        return attributeTranslateService;
    }
}
