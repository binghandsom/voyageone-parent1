package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.CmsBtDataAmountService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/7/5.
 */
@Service
public class CmsDataAmountService extends BaseTaskService {

    @Autowired
    CmsBtDataAmountService cmsBtDataAmountService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsDataAmountJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info("CmsDataAmountJob:   begin");
        // 获取允许运行的渠道
       // List<TaskControlBean> charnelList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);
        Set<String> colList = mongoTemplate.getCollectionNames();
        List<String> orderChannelIdList = colList.stream().filter(s -> s.indexOf("cms_bt_product_c") != -1 && s.length() == 19).map(s1 -> s1.substring(16)).collect(Collectors.toList());
        //$info("channel_id=" + TaskControlEnums.Name.order_channel_id);
        $info("orderChannelIdList=" + orderChannelIdList.size());
        // 根据订单渠道运行
        for ( String channelId : orderChannelIdList) {
            $info("sumByChannelId:%s begin",channelId);
                cmsBtDataAmountService.sumByChannelId(channelId);
            $info("sumByChannelId:%s end" ,channelId);

        }
        $info("CmsDataAmountJob:    end");
    }
}
