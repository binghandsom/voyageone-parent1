package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Autowired
    CmsBtProductDao cmsBtProductDao;

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
    public void testCreateUpdateSkuIdsInfo() throws Exception {

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

        // 根据京东商品id取得京东平台上的sku信息列表(即使出错也不报出来，算上新成功，只是回写出错，以后再回写也可以)
        String wareIds = "1955562601,1956338285,1956343460,1956346635,1956850725,1956856320,1956847324,1956848623,1956853915,1956849824,1956849928,1956846729,1956854153,1956860243,1956864623,1956848426,1956847257,1956848741,1956854473,1956852935,1956846867,1956853961,1956854523,1956846576,1956851530,1956847945,1956847327,1956847033,1956847132,1956849825,1956852824,1956852618,1956846027,1956852924,1956848664,1956855450";
        String[] wareIdArray = wareIds.split(",");
        StringBuffer failCause = new StringBuffer("");
        List<Sku> skus;
        System.out.println("");
        System.out.println("=============================================================");
        System.out.println("wareId      skuCode      jdSkuId");
        System.out.println("----------------------------------");
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

    @Test
    public void testCreateUpdateSkuIdsSql() throws Exception {

        String channelId = "928";
        int cartId = 28;

        System.out.println("");
//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id(channelId);
        shopBean.setCart_id(StringUtils.toString(cartId));
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        // 取得mongoDB中对象product表里面所有已上过京东平台的pNumIId(去掉重复项)
        JongoQuery queryObj = new JongoQuery();
        queryObj.setQuery("{'platforms.P"+StringUtils.toString(cartId)+".pNumIId':{$nin:[null,'']}}");
        queryObj.setProjectionExt("platforms.P"+ StringUtils.toString(cartId) +".pNumIId");
        List<CmsBtProductModel> productList = cmsBtProductDao.select(queryObj, channelId);
        if (ListUtils.isNull(productList)) {
            System.out.println("在product表中没有查到pNumIId!");
        }

        // 保存商品id(pNumIId)的列表
        List<String> wareIdList = new ArrayList<>();
        productList.forEach(p -> {
            if (!wareIdList.contains(p.getPlatformNotNull(cartId).getpNumIId())) {
                wareIdList.add(p.getPlatformNotNull(cartId).getpNumIId());
            }
        });

        StringBuilder sbUpdateSql = new StringBuilder();
        List<List<String>> pageList = CommonUtil.splitList(wareIdList, 100);
        List<Sku> skus;
        StringBuffer failCause = new StringBuffer("");
        System.out.println("=============================================================");
        for(List<String> page : pageList) {
            for (String wareId : page) {
                try {
                    skus = jdSkuService.getSkusByWareId(shopBean, wareId, failCause);

                    if (ListUtils.isNull(skus)) {
                        System.out.println(String.format("wareId(%s)没取到skus信息!", wareId));
                    }

                    if (StringUtils.isEmpty(sbUpdateSql.toString())) {
                        sbUpdateSql.append("// =============================================================\n");
                    }

                    skus.forEach(s -> {
                        System.out.println(String.format("%s\t%s\t%s", wareId, s.getOuterId(), s.getSkuId()));
                        // db.cms_bt_product_c012.update({'platforms.P23.skus.skuCode':'BAN1U761-E1S-M'},{$set:{'platforms.P23.skus.$.jdSkuId':'11110000'}})
                        sbUpdateSql.append(String.format("db.cms_bt_product_c%s.update({'platforms.P%s.skus.skuCode':'%s'}," +
                                        "{$set:{'platforms.P%s.skus.$.jdSkuId':'%s'}});\n", channelId, StringUtils.toString(cartId),
                                StringUtils.toString(s.getOuterId()), StringUtils.toString(cartId), StringUtils.toString(s.getSkuId())));
                    });
                } catch (Exception e) {
                    System.out.println("出现异常 wareId="+wareId);
                }

            }
        }
        if (!StringUtils.isEmpty(sbUpdateSql.toString())) {
            sbUpdateSql.append("// =============================================================\n");
        }
        System.out.println("=============================================================");

        String folder = "D:\\自动生成文件\\01_颜色尺寸sku\\";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentTime = df.format(System.currentTimeMillis());
        String sqlFileName = folder + "updateMongoDbJdSkuIds_" + channelId + "_" + cartId + "_" + currentTime + ".sql";

        try {
            File file = new File(sqlFileName);
            // 文件不存在，创建文件
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(sbUpdateSql.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
