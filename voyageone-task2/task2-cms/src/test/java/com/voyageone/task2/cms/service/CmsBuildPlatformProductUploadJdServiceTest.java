package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
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

    @Autowired
    JdSkuService jdSkuService;

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
        int cartId = 28;

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(762584);
        workload.setChannelId(likingChannelId);   // "928"
        workload.setCartId(cartId);               // "29","28","27"
        workload.setGroupId(Long.parseLong("1314060"));
        workload.setPublishStatus(0);

        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        uploadJdService.doChannelConfigInit(likingChannelId, cartId, channelConfigValueMap);

        uploadJdService.uploadProduct(workload, shopProp, channelConfigValueMap);
    }

    @Test
    public void testUpdateSkuIds() throws Exception {

        String likingChannelId = "928";
        int cartId = 28;

//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id(likingChannelId);
        shopBean.setCart_id(StringUtils.toString(cartId));
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        String numIId = "1956342136";
        uploadJdService.updateSkuIds(shopBean, numIId, true);
    }

    @Test
    public void testCreateUpdateSkuIdsSql() throws Exception {

        String likingChannelId = "928";
        int cartId = 28;

//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
        shopBean.setSessionKey("4326ace5-57d7-4b9e-b24a-3ac2471eabe3"); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id(likingChannelId);
        shopBean.setCart_id(StringUtils.toString(cartId));
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        // 根据京东商品id取得京东平台上的sku信息列表(即使出错也不报出来，算上新成功，只是回写出错，以后再回写也可以)
        String wareIds = "1955562601,1956338285,1956850725,1956856320,1956847324,1956848623,1956853915,1956849824,1956849928,1956846729,1956854153,1956848426,1956847257,1956848741,1956852935,1956846867,1956853961,1956854523,1956846576,1956851530,1956847945,1956847327,1956847033,1956847132,1956849825,1956852824,1956852618,1956846027,1956852924,1956848664,1956855450";
        String[] wareIdArray = wareIds.split(",");
        StringBuffer failCause = new StringBuffer("");
        List<Sku> skus;
        System.out.println("");
        System.out.println("=============================================================");
        System.out.println("wareId      skuCode      jdSkuId");
        System.out.println("---------------------------------");
        for (String wareId : wareIdArray) {

            skus = jdSkuService.getSkusByWareId(shopBean, wareId, failCause);

            if (ListUtils.isNull(skus)) {
                System.out.println(String.format("wareId(%s)没取到skus信息!", wareId));
            }

            skus.forEach(s -> {
                System.out.println(String.format("%s\t%s\t%s", wareId, s.getOuterId(), s.getSkuId()));
            });

        }
        System.out.println("=============================================================");
    }
}
