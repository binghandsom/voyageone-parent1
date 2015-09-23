package com.voyageone.batch.synship.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.synship.service.GetClientShippingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取品牌方的物流信息
 *
 * Created by jonas on 15/6/1.
 */
@Component("getClientTrackingTask")
public class GetClientTrackingJob extends BaseTaskJob {

    @Autowired
    private GetClientShippingInfoService getClientShippingInfoService;

    @Override
    protected BaseTaskService getTaskService() {
        return getClientShippingInfoService;
    }
}
