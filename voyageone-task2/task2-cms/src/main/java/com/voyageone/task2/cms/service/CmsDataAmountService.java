package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtDataAmountService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2016/7/5.
 */
@Service
public class CmsDataAmountService extends BaseTaskService {

    @Autowired
    CmsBtDataAmountService cmsBtDataAmountService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsDataAmountService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 获取允许运行的渠道
        List<TaskControlBean> charnelList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        $info("channel_id=" + TaskControlEnums.Name.order_channel_id);
        $info("orderChannelIdList=" + charnelList.size());

        // 根据订单渠道运行
        for (final TaskControlBean taskControlBean : charnelList) {
            if (taskControlBean.getCfg_val2() != null && taskControlBean.getCfg_val2().equals("1")) {
                cmsBtDataAmountService.sumByChannelId(taskControlBean.getCfg_val1());
            }
        }
    }
}
