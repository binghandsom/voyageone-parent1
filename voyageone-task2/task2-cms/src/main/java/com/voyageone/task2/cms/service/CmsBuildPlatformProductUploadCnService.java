package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 独立域名产品上新
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformProductUploadCnService extends BaseTaskService {

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnJob";
    }

    /**
     * 独立域名上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }
}
