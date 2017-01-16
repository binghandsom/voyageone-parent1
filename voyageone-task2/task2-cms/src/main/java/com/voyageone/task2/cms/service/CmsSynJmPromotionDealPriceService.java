package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionSkuService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.task2.base.BaseCronTaskService;
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
public class CmsSynJmPromotionDealPriceService extends BaseCronTaskService {

    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;

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
        if (jmPromotionIds != null) {

            jmPromotionIds.forEach(jmPromotionId -> {
                if (jmPromotionId > 0) {
                    try {
                        cmsBtJmPromotionSkuService.senderJMRefreshPriceMQMessage(jmPromotionId, "定时自动刷新");
                    } catch (MQMessageRuleException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
