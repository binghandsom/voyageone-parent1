package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.service.clientInventory.*;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by sky on 2015/7/29.
 */
@Service
@Transactional
public class WmsGetClientInvService extends BaseTaskService {

    @Autowired
    WmsGetCAClientInvService wmsGetCAClientInvService;

    @Autowired
    WmsGetJewelryClientInvService wmsGetJewelryClientInvService;

    @Autowired
    WmsGetSearsClientInvService wmsGetSearsClientInvService;

    @Autowired
    WmsGetWmfClientInvService wmsGetWmfClientInvService;

    @Autowired
    WmsGetGiltClientInvService wmsGetGiltClientInvService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
            return "wmsGetClientInvJob";
    }

    protected void onStartup(final List<TaskControlBean> taskControlList) throws Exception {
        log(getTaskName() + "----------开始");
        //允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        //线程
        List<Runnable> threads = new ArrayList<>();
        for(String channelId : orderChannelIdList){
            if (channelId.equals(ChannelConfigEnums.Channel.BHFO.getId())) {
                wmsGetCAClientInvService.sysCAInventoryByClient(channelId, threads);
            }
            else if (channelId.equals(ChannelConfigEnums.Channel.JEWELRY.getId())) {
                wmsGetJewelryClientInvService.sysCAInventoryByClient(channelId, threads);
            }
            else if (channelId.equals(ChannelConfigEnums.Channel.SEARS.getId())) {
                wmsGetSearsClientInvService.sysSearsInventoryByClient(channelId, threads);
            }
            else if(channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
                wmsGetWmfClientInvService.sysWmfInventoryByClient(channelId, threads);
            }
            else if(channelId.equals(ChannelConfigEnums.Channel.GILT.getId())) {
                wmsGetGiltClientInvService.sysGiltInventoryByClient(channelId, threads);
            }

        }
        runWithThreadPool(threads, taskControlList);
        log(getTaskName() + "----------结束");
    }

    protected void log (String logMsg){
        logger.info("===============" + logMsg + "===============");
    }



}
