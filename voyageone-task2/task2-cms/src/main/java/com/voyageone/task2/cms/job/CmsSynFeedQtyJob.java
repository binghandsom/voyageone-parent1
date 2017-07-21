package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.sneakerhead.CmsSynFeedQtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by james on 2017/7/12.
 * 同步feed里面的库存
 */
@Component("CmsSynFeedQtyJob")
public class CmsSynFeedQtyJob extends BaseTaskJob {

    @Autowired
    private CmsSynFeedQtyService cmsSynFeedQtyService;
    @Override
    protected BaseTaskService getTaskService() {
        return cmsSynFeedQtyService;
    }
}
