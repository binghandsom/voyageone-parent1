package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author james.li on 2016/2/16.
 * @version 2.0.0
 */
@Service
public class CmsPromotrionService extends BaseTaskService{
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsPromotionJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {


    }

//    private get
}
