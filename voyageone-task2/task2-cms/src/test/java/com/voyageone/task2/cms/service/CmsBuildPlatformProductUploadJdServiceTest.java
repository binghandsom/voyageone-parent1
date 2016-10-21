package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by desmond on 2016/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJdServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadJdService uploadJdService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadJdJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("928");
        tcb.setTask_comment("京东国际Liking店上新允许运行的渠道");
        taskControlList.add(tcb);
        uploadJdService.onStartup(taskControlList);
    }

    @Test
    public void testUploadProduct() throws Exception {

        String likingChannelId = "928";
        int cartId = 29;

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(762584);
        workload.setChannelId(likingChannelId);   // "928"
        workload.setCartId(cartId);               // "29","28","27"
        workload.setGroupId(Long.parseLong("919460"));
        workload.setPublishStatus(0);

        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        uploadJdService.doChannelConfigInit(likingChannelId, cartId, channelConfigValueMap);

        uploadJdService.uploadProduct(workload, shopProp, channelConfigValueMap);
    }
}
