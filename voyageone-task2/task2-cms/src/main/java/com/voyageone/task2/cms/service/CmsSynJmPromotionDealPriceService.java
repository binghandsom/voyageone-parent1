package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionSkuService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by james on 2016/10/19.
 */
@Service
public class CmsSynJmPromotionDealPriceService extends BaseCronTaskService {

    @Autowired
    CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;

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
                    cmsBtJmPromotionSkuService.senderJMRefreshPriceMQMessage(jmPromotionId, "定时自动刷新", "000");
                }
            });
        }
    }
}
