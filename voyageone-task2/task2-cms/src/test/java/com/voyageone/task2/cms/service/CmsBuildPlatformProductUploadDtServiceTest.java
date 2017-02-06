package com.voyageone.task2.cms.service;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang.math.NumberUtils;
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
 * 分销上新测试
 *
 * Created by desmond on 2016/12/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadDtServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadDtService uploadDtService;

    @Autowired
    JdSkuService jdSkuService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testOnStartup() throws Exception {
        List<TaskControlBean> taskControlList = new ArrayList<>();
        TaskControlBean tcb = new TaskControlBean();
        tcb.setTask_id("CmsBuildPlatformProductUploadDtJob");
        tcb.setCfg_name("order_channel_id");
        tcb.setCfg_val1("928");
        tcb.setTask_comment("京东国际Liking店上新允许运行的渠道");
        taskControlList.add(tcb);
        TaskControlBean tcbThreadCount = new TaskControlBean();
        tcbThreadCount.setTask_id("CmsBuildPlatformProductUploadDtJob");
        tcbThreadCount.setCfg_name("thread_count");
        tcbThreadCount.setCfg_val1("3");
        tcbThreadCount.setTask_comment("最大线程数");
        taskControlList.add(tcbThreadCount);
        TaskControlBean tcbRowCount = new TaskControlBean();
        tcbRowCount.setTask_id("CmsBuildPlatformProductUploadDtJob");
        tcbRowCount.setCfg_name("row_count");
        tcbRowCount.setCfg_val1("100");
        tcbRowCount.setTask_comment("一次最大抽出件数");
        taskControlList.add(tcbRowCount);

        uploadDtService.onStartup(taskControlList);
        System.out.println("testOnStartup 测试正常结束!");
    }

    @Test
    public void testUploadProduct() throws Exception {
        // 清除缓存（cms.channel_config表）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());

        // viw_com_cart_shop_channel_mapping的View里面可以看到有哪些渠道会上分销平台(用cart_id=33过滤)
        String likingChannelId = "928";
        int cartId = NumberUtils.toInt(CartEnums.Cart.DT.getId());   // 33

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(864987);
        workload.setChannelId(likingChannelId);   // "928"
        workload.setCartId(cartId);               // "33"
        workload.setGroupId(Long.parseLong("864987"));
        workload.setPublishStatus(CmsConstants.SxWorkloadPublishStatusNum.initNum);   // 普通上新模式
        workload.setModifier("desmond");

//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url("http://10.0.1.39:8082/dist");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey(""); // 分销测试店(SessionKey)
        shopProp.setOrder_channel_id(likingChannelId);
        shopProp.setCart_id(StringUtils.toString(cartId));
        shopProp.setShop_name("分销测试店");
        shopProp.setPlatform_id(PlatFormEnums.PlatForm.DT.getId());  // 分销平台("6")

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();

        uploadDtService.uploadProduct(workload, shopProp, channelConfigValueMap);
        System.out.println("testUploadProduct 测试正常结束!");
    }

}
