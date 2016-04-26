package com.voyageone.task2.cms.service.imagecreate;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.imagecreate.AliYunOSSFileService;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/26.
 */
public class AliYunOSSJobService extends BaseMQTaskService {

    @Autowired
    AliYunOSSFileService service;
    private static final Logger LOG = LoggerFactory.getLogger(AliYunOSSJobService.class);
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {

    }
    @Override
    public SubSystem getSubSystem() {
            return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return "AliYunOSSJobService";
    }
}
