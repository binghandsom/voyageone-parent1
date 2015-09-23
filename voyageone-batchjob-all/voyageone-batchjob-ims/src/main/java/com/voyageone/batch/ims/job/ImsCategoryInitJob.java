package com.voyageone.batch.ims.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.ims.service.ImsCategoryInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 主数据类目和属性设定
 *
 * @author Jack
 */
@Component("ImsCategoryInitTask")
public class ImsCategoryInitJob extends BaseTaskJob {

    @Autowired
    ImsCategoryInitService imsCategoryInitService;

    @Override
    protected BaseTaskService getTaskService() {
        return imsCategoryInitService;
    }

}
