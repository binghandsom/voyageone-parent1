package com.voyageone.batch.synship.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.service.clientShippingInfo.GetJewelryClientShippingInfoService;
import com.voyageone.batch.synship.service.clientShippingInfo.GetWmfClientShippingInfoService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2016-01-07.
 */
@Service
public class GetClientShippingInfoService extends BaseTaskService {

    @Autowired
    GetJewelryClientShippingInfoService getJewelryClientShippingInfoService;

    @Autowired
    GetWmfClientShippingInfoService getWmfClientShippingInfoService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SynshipGetClientShipping";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info(getTaskName() + "----------开始");
        //允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        //线程
        List<Runnable> threads = new ArrayList<>();
        for(String channelId : orderChannelIdList){
            if (channelId.equals(ChannelConfigEnums.Channel.JEWELRY.getId())) {
                getJewelryClientShippingInfoService.getJewelryShippingInfo(channelId, threads);
            }
            else if (channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
                getWmfClientShippingInfoService.getWmfShippingInfo(channelId, threads);
            }

        }
        runWithThreadPool(threads, taskControlList);
        $info(getTaskName() + "----------结束");

    }

}
