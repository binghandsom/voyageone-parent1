package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by charis on 2017/2/23.
 */
@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJdNewServiceTest {

    @Autowired
    CmsBuildPlatformProductUploadJdNewService uploadJdService;

    @Autowired
    JdSkuService jdSkuService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    SxProductService sxProductService;

    @Autowired
    CmsPlatformTitleTranslateMqService cmsTranslateMqService;
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
        // 清除缓存（cms.channel_config表）
//        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString());  // 最好不用这种方法清缓存
//        TypeChannels.reload();   // 最好用reload方法清理缓存

        String snChannelId = "928";
        int cartId = 28;

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
//        workload.setId(864987);
        workload.setChannelId(snChannelId);   // "928"
        workload.setCartId(cartId);               // "26"
        workload.setGroupId(Long.parseLong("1323281"));
        workload.setPublishStatus(CmsConstants.SxWorkloadPublishStatusNum.initNum);   // 普通上新模式
//        workload.setPublishStatus(CmsConstants.SxWorkloadPublishStatusNum.smartSx);   // 智能上新模式
        workload.setModifier("charis");

//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopProp = new ShopBean();
        shopProp.setApp_url("https://api.jd.com/routerjson");
        shopProp.setAppKey("");
        shopProp.setAppSecret("");
        shopProp.setSessionKey(""); // 匠心界全球购专营店(SessionKey)
        shopProp.setOrder_channel_id(snChannelId);
        shopProp.setCart_id(StringUtils.toString(cartId));
        shopProp.setShop_name("匠心界全球购专营店");
//        shopProp.setShop_name("京东国际悦境店");
        shopProp.setPlatform_id("2");

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
        uploadJdService.doChannelConfigInit(snChannelId, cartId, channelConfigValueMap);

        uploadJdService.uploadProduct(workload, shopProp, channelConfigValueMap, null);
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
        uploadJdService.updateSkuIds(shopBean, numIId, true, 0L);
    }

//    @Test
//    public void testIsSkuNoStock() throws Exception {
//        // 测试判断SKU逻辑库存是否为0的方法
//
//        Map<String, Integer> skuStockMap = new HashMap() {{
//            put("skuCode1", 1);
//            put("skuCode2", 0);
//            put("skuCode3", 2);
//            put("skuCode4", 3);
//        }};
//
//        System.out.println("测试结果:");
//        System.out.println("skuNotExistCode = " + (uploadJdService.isSkuNoStock("skuNotExistCode", skuStockMap) ? "true" : "false"));
//        System.out.println("skuCode1 = " + (uploadJdService.isSkuNoStock("skuCode1", skuStockMap) ? "true" : "false"));
//        System.out.println("skuCode2 = " + (uploadJdService.isSkuNoStock("skuCode2", skuStockMap) ? "true" : "false"));
//        System.out.println("skuCode3 = " + (uploadJdService.isSkuNoStock("skuCode3", skuStockMap) ? "true" : "false"));
//        System.out.println("skuCode4 = " + (uploadJdService.isSkuNoStock("skuCode4", skuStockMap) ? "true" : "false"));
//        System.out.println("skuCode5 = " + (uploadJdService.isSkuNoStock("skuCode5", skuStockMap) ? "true" : "false"));
//        System.out.println("测试结束!");
//    }

    @Test
    public void testDoJdForceWareListing() throws Exception {
        // 强制上下架，并回写状态测试

        String likingChannelId = "928";
        int cartId = 28;
        Long wareId = 1956992392L;
        Long groupId = 864987L;

//        ShopBean shopProp = Shops.getShop(likingChannelId, cartId);   // "928", "29"
        ShopBean shopBean = new ShopBean();
        shopBean.setApp_url("https://api.jd.com/routerjson");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
        shopBean.setOrder_channel_id(likingChannelId);
        shopBean.setCart_id(StringUtils.toString(cartId));
        shopBean.setShop_name("京东国际匠心界全球购专营店");

        // 测试用库存为0的SKU列表
        List<String> codeList = new ArrayList() {{
            add("022-EA3060501754");
            add("022-EA3060538652");
//            add("022-EA3060538852");
        }};

        System.out.println("京东强制上下架测试结果:");
        uploadJdService.doJdForceWareListing(shopBean, wareId, groupId, CmsConstants.PlatformActive.ToInStock, codeList, true, "京东上新SKU总库存为0时强制下架处理");
        System.out.println("京东强制上下架测试结束!");
    }

    @Test
    public void testDeleteJdPlatformSku() throws Exception {
        // 删除根据jdSkuId删除京东平台上商品SKU的测试

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

        // 测试用库存为0的SKU列表
        List<String> skuIdListNoStock = new ArrayList() {{
            add("1973738864");
            add("1973738862");
            add("1979999999");
        }};

        System.out.println("测试结果:");
        uploadJdService.deleteJdPlatformSku(shopBean, skuIdListNoStock);
        System.out.println("测试结束!");
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
        String wareIds = "1957082836,1957088131,1957089112,1957082744,1957087025,1957088515,1957084445,1957086825,1957084633,1957084921,1957138368,1957139645,1957142647,1957141547,1957143847,1957142955,1957139646,1957141743,1957139948,1957141546,1957143041,1957138654,1957140948,1957143044,1957138552,1957142559,1957143248,1957141340,1957138751,1957141248,1957143848,1957137178,1957139853,1957140842,1957138049,1957141246,1957137354,1957141740";
        String[] wareIdArray = wareIds.split(",");
        StringBuilder failCause = new StringBuilder("");
        List<Sku> skus;
        System.out.println("");
        System.out.println("=============================================================");
        System.out.println("wareId\tskuCode\tjdSkuId");
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
        // 批量取得product表中所有已上过京东，但没有回写过jdSkuId的numIId，然后取得所有的jdSkuId生成mongo更新sql文

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
        queryObj.setProjectionExt("platforms.P"+ StringUtils.toString(cartId) +".pNumIId","platforms.P"+ StringUtils.toString(cartId) +".skus");
        List<CmsBtProductModel> productList = cmsBtProductDao.select(queryObj, channelId);
        if (ListUtils.isNull(productList)) {
            System.out.println("在product表中没有查到pNumIId!");
        }

        // 保存商品id(pNumIId)的列表
        // 这里取得的pNumIid也不一定就是没有回写过jdSkuId的，因为有可能同一个group下面product1的jdSkuId回写了，product2的jdSkuId在平台上没有这个sku不用回写
        List<String> wareIdList = new ArrayList<>();
        productList.forEach(p -> {
            // 如果该产品platforms.PXX.skus里面有isSale=true并且jdSkuId为空的时候(并且没有一个jdSkuId都没有,如果不想这样就把这个条件注掉)
            if ((p.getPlatformNotNull(cartId).getSkus().stream().filter(s -> Boolean.parseBoolean(s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name())) && StringUtils.isEmpty(s.getStringAttribute("jdSkuId"))).count() > 0)
               && (p.getPlatformNotNull(cartId).getSkus().stream().filter(s -> Boolean.parseBoolean(s.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.isSale.name())) && !StringUtils.isEmpty(s.getStringAttribute("jdSkuId"))).count() == 0)) {
                if (!wareIdList.contains(p.getPlatformNotNull(cartId).getpNumIId())) {
                    wareIdList.add(p.getPlatformNotNull(cartId).getpNumIId());
                }
            }
        });

        List<String> successList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();

        StringBuilder sbUpdateSql = new StringBuilder();
        List<List<String>> pageList = CommonUtil.splitList(wareIdList, 100);
        List<Sku> skus;
        StringBuilder failCause = new StringBuilder("");

        String folder = "D:\\自动生成文件\\02_回写jdSkuId\\";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentTime = df.format(System.currentTimeMillis());
        String sqlFileName = folder + "updateMongoDbJdSkuIds_" + channelId + "_" + cartId + "_" + currentTime + ".sql";

        System.out.println("=============================================================");
        System.out.println("件数    wareId      skuCode             jdSkuId");
        System.out.println("---------------------------------------------------");
        int currentBatchCnt = 1;
        int currentCnt = 0;
        int totalCnt = wareIdList.size();
        for(List<String> page : pageList) {
            sbUpdateSql.append("// ====第"+currentBatchCnt+"/"+pageList.size()+"批("+(currentCnt+1)+"~"+(currentCnt + page.size())+")件(总件数:"+totalCnt+"件)商品ID对应的jdSkuIds======\n");
            for (String wareId : page) {
                currentCnt++;
                try {
                    skus = jdSkuService.getSkusByWareId(shopBean, wareId, failCause);

                    if (ListUtils.isNull(skus)) {
                        System.out.println(String.format("第("+currentCnt+"/"+totalCnt+")条wareId(%s)没取到skus信息!", wareId));
                    }

                    for (Sku s : skus) {
                        System.out.println(String.format(currentCnt+"/"+totalCnt+"\t%s\t%s\t%s", wareId, s.getOuterId(), s.getSkuId()));
                        // db.cms_bt_product_c012.update({'platforms.P23.skus.skuCode':'BAN1U761-E1S-M'},{$set:{'platforms.P23.skus.$.jdSkuId':'11110000'}})
                        sbUpdateSql.append(String.format("db.cms_bt_product_c%s.update({'platforms.P%s.skus.skuCode':'%s'}," +
                                        "{$set:{'platforms.P%s.skus.$.jdSkuId':'%s'}});\n", channelId, StringUtils.toString(cartId),
                                StringUtils.toString(s.getOuterId()), StringUtils.toString(cartId), StringUtils.toString(s.getSkuId())));
                    }
                    successList.add(wareId);
                } catch (Exception e) {
                    System.out.println("取得第"+currentCnt+"/"+totalCnt+"件商品id(wareId:"+wareId+")的jdSkuId时出现异常!");
                    errorList.add(wareId);
                }

            }

            if (currentBatchCnt == pageList.size()) {
                // 最后一次加上完成说明
                sbUpdateSql.append("// ====("+currentCnt+"/"+totalCnt+")件商品的jdSkuId全部取得完成==========================\n");
            }

            try {
                File file = new File(sqlFileName);
                // 文件不存在，创建文件
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(sbUpdateSql.toString());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sbUpdateSql.delete(0, sbUpdateSql.length());
            currentBatchCnt++;
        }
        System.out.println("==================================================================");
        System.out.println("ChannelId:"+channelId+" CartId:"+cartId+"需要取得jdSkuId的商品(wareId)总件数为：" + totalCnt + "件");
        System.out.println("1 成功取得jdSkuId商品件数\t" + successList.size()   + "件\t" + successList.stream().collect(Collectors.joining(",")));
        System.out.println("2 未能成功取得jdSkuId商品件数\t" + errorList.size()   + "件\t" + errorList.stream().collect(Collectors.joining(",")));
        System.out.println("==================================================================");
        System.out.println("自动生成的jdSkuId更新SQL文： " + sqlFileName);

    }
    @Test
    public void testUpdateImsBtProduct() {
        String channelId = "928";
        long groupId = 1323281L;
        SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

        if (sxData == null) {
            System.out.println("sxData == null");
            return;
        }

        sxProductService.updateImsBtProduct(sxData, "charis");

    }

    @Test
    public void testDate() throws ParseException {

        Date changeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-05-28 00:00:00");
        Date nowTime  = new Date();

        System.out.println("是否到28号0点了？   " + changeTime.before(nowTime));
    }

    @Test
    public void testQty() throws Exception{

//        Map<String, Integer> skuQtyMap = sxProductService.getAvailQuantity("928", "27", null, "7307995");
//
//        System.out.println("hehe");

        String skus = "[{\"hscode\":\"3504009000\",\"outer_id\":\"017-53392\",\"price\":\"144.0\",\"quantity\":3031},{\"hscode\":\"3504009000\",\"outer_id\":\"017-53391\",\"price\":\"144.0\",\"quantity\":3030}]";
        List<Map<String, Object>> skuPageQtyMapList = JacksonUtil.jsonToMapList(skus);
        Map<String, String> skuQtyMap = new HashMap<>();
        skuPageQtyMapList.stream()
                .forEach(sku -> {
                    skuQtyMap.put(sku.get("outer_id").toString().toLowerCase(), sku.get("quantity").toString());
                });
        System.out.println(skuQtyMap);
    }

    @Test
    public void testTranslateTitle() {
        String channelId = "928";
        long groupId = 1323281L;
        SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

        System.out.println(sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().getStringAttribute("productTitle"));

        sxData = cmsTranslateMqService.executeSingleCode(sxData, "1");

        System.out.println(sxData.getMainProduct().getPlatform(sxData.getCartId()).getFields().getStringAttribute("productTitle"));
    }
}
