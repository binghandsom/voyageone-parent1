package com.voyageone.task2.cms.service.jumei;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.jumei.platefrom.JuMeiDealService;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/14.
 */
@Service
public class JuMeiProductUpdateDealEndTimeJobService extends BaseMQTaskService {
    @Autowired
    JuMeiDealService service;
    private static final Logger LOG = LoggerFactory.getLogger(JmBtPromotionImportJobService.class);
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        int id = (int) Double.parseDouble(message.get("id").toString());
        service.updateDealEndTime(id);
    }
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return "JuMeiProductUpdateDealEndTimeJobService";
    }

}
