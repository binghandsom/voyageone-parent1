package com.voyageone.task2.cms.service.product;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取商品在平台的实际上下架状态
 *
 * @author jason.jiang on 2016/08/03
 * @version 2.0.0
 */
@Service
public class CmsGetPlatformStatusService extends BaseTaskService {


    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetPlatformStatusJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }

}
