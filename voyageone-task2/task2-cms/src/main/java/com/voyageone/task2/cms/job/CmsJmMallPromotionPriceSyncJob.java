package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.jumei.JmMallPromotionPriceSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 聚美活动开始 把商城的价格设置为活动价
 * Created by jiangjusheng on 2016/10/18.
 */
@Component("CmsJmMallPromotionPriceSyncJob")
public class CmsJmMallPromotionPriceSyncJob extends BaseTaskJob {

    @Autowired
    JmMallPromotionPriceSyncService targetService;

    @Override
    protected BaseTaskService getTaskService() {
        return targetService;
    }
}
