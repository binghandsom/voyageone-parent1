package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/10/19.
 */
@Service
public class CmsSynJmPromotionDealPriceService extends BaseTaskService {

    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;

    @Autowired
    private MqSender sender;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSynJmPromotionDealPriceJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        List<Integer> jmPromotionIds = cmsBtJmPromotionService.selectEffectiveJmPromotionId();
        if(jmPromotionIds != null){
            jmPromotionIds.forEach(jmPromotionId ->{
                Map<String,Object> param = new HashedMap();
                param.put("jmPromotionId",jmPromotionId);
                sender.sendMessage(MqRoutingKey.CMS_BATCH_JmSynPromotionDealPrice, param);
            });
        }
    }
}
