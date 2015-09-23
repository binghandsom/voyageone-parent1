package com.voyageone.batch.ims.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.ims.service.ImsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 第三方平台类目和属性设定
 *
 * @author Jack
 */
@Component("ImsCategoryTask")
public class ImsCategoryJob extends BaseTaskJob {

    @Autowired
    ImsCategoryService imsCategoryService;

    @Override
    protected BaseTaskService getTaskService() {
        return imsCategoryService;
    }

}
