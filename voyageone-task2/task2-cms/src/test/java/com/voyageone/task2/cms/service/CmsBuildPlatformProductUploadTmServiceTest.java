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

/**
 * Created by desmond on 2016/6/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadTmServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadTmService uploadTmService;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadTmJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("017");
        tcb.setTask_comment("天猫国际PortAmerican海外专营店上新允许运行的渠道");
        taskControlList.add(tcb);
        uploadTmService.onStartup(taskControlList);
    }

    @Test
    public void testUploadProduct() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(247);
        workload.setChannelId("012");
        workload.setCartId(23);
//        workload.setGroupId(Long.parseLong("977667"));   // 服饰
//        workload.setGroupId(Long.parseLong("980134"));   // 配饰1
        workload.setGroupId(Long.parseLong("10798126"));   // 配饰2
//        workload.setGroupId(Long.parseLong("984304"));   // 鞋子
        workload.setPublishStatus(0);

        ShopBean shopProp = Shops.getShop("012", "23");

        uploadTmService.uploadProduct(workload, shopProp);
    }

    @Test
    public void testUploadProduct2() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
//        workload.setId(247);
        workload.setChannelId("018");
        workload.setCartId(23);
        workload.setGroupId(Long.parseLong("494011"));
        workload.setPublishStatus(0);

        // 测试用PortAmerican海外专营店
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id("018");
        shop.setCart_id("23");
        shop.setApp_url("http://gw.api.taobao.com/router/rest");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        // platformid一定要设成京东，否则默认为天猫（1）的话，expressionParser.parse里面会上传照片到天猫空间，出现异常
        shop.setPlatform_id("1");

        uploadTmService.uploadProduct(workload, shop);
    }
}
