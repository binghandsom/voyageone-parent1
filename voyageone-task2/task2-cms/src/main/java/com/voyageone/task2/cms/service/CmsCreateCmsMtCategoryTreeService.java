package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.CategoryTreeService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author james.li on 2016/2/22.
 * @version 2.0.0
 */
@Service
public class CmsCreateCmsMtCategoryTreeService extends BaseTaskService {

    @Autowired
    CategoryTreeService cmsMtCategoryTree;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsCreateCmsMtCategoryTreeJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equalsIgnoreCase(taskControl.getCfg_name())) {
                cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform(taskControl.getCfg_val1(), Integer.parseInt(taskControl.getCfg_val2()));
            }
        }

    }
}
