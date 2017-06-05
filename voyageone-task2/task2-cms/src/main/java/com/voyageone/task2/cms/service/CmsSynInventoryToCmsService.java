package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionProduct3Service;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class CmsSynInventoryToCmsService extends BaseCronTaskService {

    @Autowired
    CmsBtJmPromotionProduct3Service cmsBtJmPromotionProduct3Service;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSynInventoryToCmsJob";
    }

    @Override
    public boolean getLogWithThread() {
        return true;
    }

    /**
     * 批量插入code级别的库存数据到mongdodb，以便db端的定时任务进行处理
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 同步库存数据到聚美活动表 cms_bt_jm_promotion_product
        cmsBtJmPromotionProduct3Service.sendMessageJmPromotionProductStockSync(this.getTaskName());
    }

}
