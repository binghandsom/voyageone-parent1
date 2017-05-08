package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuAddInfo;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.components.jumei.bean.JmGetProductInfoRes;
import com.voyageone.components.jumei.bean.JmGetProductInfo_Spus;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:context-cms-test.xml")
public class CmsBuildPlatformProductUploadJMServiceTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CmsBuildPlatformProductUploadJMService cmsBuildPlatformProductUploadJMService;
    @Autowired
    private JumeiHtMallService jumeiHtMallService;

    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Autowired
    CmsBtProductGroupDao cmsBtProductGroupDao;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsBtJmSkuDao cmsBtJmSkuDao;

    @Autowired
    SxProductService sxProductService;

    @Autowired
    JumeiProductService jumeiProductService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductGroupService productGroupService;

    @Test
    public void TestPrice() throws Exception {


    }



    @Test
    public void TestDate() throws Exception {



        Map<String, String> map = new HashMap<>();
        String value = map.get("1");

        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(currentTime);
        String dateStr = formatter.format(date);
        System.out.println(dateStr);

        Long time = getTime(dateStr);
        System.out.println(time);

        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MINUTE, 30);
        System.out.println(rightNow.getTimeInMillis());
        Date date1 = new Date(rightNow.getTimeInMillis());
        date1.getTime();
        String date1Str = formatter.format(date1);
        System.out.println(date1Str);

        time = getTime(date1Str);
        System.out.println(time);


    }

    private static Long getTime(String user_time) throws Exception {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d;


        d = sdf.parse(user_time);
        long l = d.getTime()/1000-8*3600;

        return l;
    }



    @Test
    public void testUpdateProduct() throws Exception {

//        List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartIdGroupBy(1, "010", 27);
//
//        for (CmsBtSxWorkloadModel work : workloadList) {
////            work.setGroupId(27214L);
////            work.setGroupId(39342L);
//            work.setGroupId(30222L);
//
//
//            cmsBuildPlatformProductUploadJMService.updateProduct(work);
//        }

        CmsBtSxWorkloadModel work = new CmsBtSxWorkloadModel();
        work.setCartId(27);
        work.setChannelId("028");
        work.setGroupId(1091269L);
        work.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(work);

    }

    @Test
    public void testUpdateProduct2() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(185);
        workload.setChannelId("028");
        workload.setCartId(27);
        workload.setGroupId(Long.parseLong("1126837"));
        workload.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(workload);

    }

    /**
     * 上新成功的数据，上传到聚美商城
     */
    @Test
    public void testUploadMallForAll() {
        String channelId = "028";
        int cartId = 27;
        ShopBean shopBean = Shops.getShop(channelId, cartId);

//        String query = "{\"cartId\": " + cartId + "}";
//        List<CmsBtProductGroupModel> listGroup = cmsBtProductGroupDao.select(query, channelId);

        List<Long> listSkipGroupId = new ArrayList<>(); // 跳过一些不上新的数据
        String[] numiids = {};

        logger.info("============ 上传聚美商城 start !!! ============");
        logger.info("channelId is " + channelId);

//        for (CmsBtProductGroupModel groupModel : listGroup) {
//            if (!StringUtils.isEmpty(groupModel.getPlatformMallId())) {
//                // 上传过，不再处理，注掉这段if的话，就支持更新了(但是注意uploadMall方法最后两个参数，null的话，不支持追加sku)
//                continue;
//            }
//            if (StringUtils.isEmpty(groupModel.getPlatformPid()) || StringUtils.isEmpty(groupModel.getNumIId())) {
//                // 没有成功上新过
//                continue;
//            }
//
//            Long groupId = groupModel.getGroupId();
//            SxData sxData;
//            try {
//                sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
//                if (sxData == null) {
//                    throw new BusinessException("SxData取得失败!");
//                }
//                if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
//                    throw new BusinessException(sxData.getErrorMessage());
//                }
//
//            } catch (Exception e) {
//                if (e instanceof BusinessException) {
//                    String errorMsg = "GroupId [" + groupId + "]跳过:" + ((BusinessException) e).getMessage();
//                    listSkipGroupId.add(groupId);
//                } else {
//                    logger.info("GroupId [" + groupId + "]SxData取得失败!" + e.getMessage());
//                }
//                continue;
//            }
//
//            ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//            CmsBtProductModel product = sxData.getMainProduct();
//            if (StringUtils.isEmpty(product.getPlatform(cartId).getpProductId()) || StringUtils.isEmpty(product.getPlatform(cartId).getpNumIId())) {
//                logger.info("GroupId [" + groupId + "] product表的产品id(pProductId)或商品id(pNumIId)为空!");
//                continue;
//            }

            for (String numiid : numiids) {
                try {
//                String mallId = cmsBuildPlatformProductUploadJMService.uploadMall(product, shop, expressionParser, null, null);
                    StringBuffer sb = new StringBuffer("");
                    String mallId = jumeiHtMallService.addMall(shopBean, numiid, sb);

                    if (StringUtils.isEmpty(mallId) || sb.length() > 0) {
                        // 上传失败
                        throw new BusinessException("添加商品到聚美商城失败!" + sb.toString());
                    }
                    logger.info(String.format("%s\t%s", numiid, mallId));
                } catch (BusinessException be) {
                    logger.info("numiid [" + numiid + "] 上传聚美商城失败-1!" + be.getMessage());
                } catch (Exception e) {
                    logger.info("numiid [" + numiid + "] 上传聚美商城失败-2!" + e.getMessage());
                }

//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
//        }

//        logger.info("跳过的groupId:" + listSkipGroupId);
        logger.info("============ 上传聚美商城 end !!! ============");
    }

    @Test
    public void testUploadMallForAllByGroup() {
        String channelId = "015";
        int cartId = 27;
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String[] groupIds = {};

        logger.info("============ 上传聚美商城 start !!! ============");
        logger.info("channelId is " + channelId);
        String numiid;
        for (String groupId : groupIds) {
            numiid = "";
            try {
                // 获取group信息
                CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
                if (grpModel == null) {
                    logger.info("没找到对应的group数据(groupId=" + groupId + ")");
                    continue;
                }

                String mallId = grpModel.getPlatformMallId();
                if (!StringUtils.isEmpty(mallId)) {
                    logger.info(String.format("已经加到聚美商城啦! groupId[%s]  mallId[%s]", groupId, mallId));
                    continue;
                }

                numiid = grpModel.getNumIId();
                if (StringUtils.isEmpty(numiid)) {
                    logger.info(String.format("numIId为空! groupId[%s]", groupId));
                    continue;
                }

                StringBuffer sb = new StringBuffer("");
                mallId = jumeiHtMallService.addMall(shopBean, numiid, sb);

                if (StringUtils.isEmpty(mallId) || sb.length() > 0) {
                    // 上传失败
                    throw new BusinessException("添加商品到聚美商城失败!" + sb.toString());
                }
                logger.info(String.format("%s\t%s\t%s", groupId, numiid, mallId));
            } catch (BusinessException be) {
                logger.info("groupId [" + groupId + "]" + " numiid [" + numiid + "] 上传聚美商城失败-1!" + be.getMessage());
            } catch (Exception e) {
                logger.info("groupId [" + groupId + "]" + " numiid [" + numiid + "] 上传聚美商城失败-2!" + e.getMessage());
            }

        }

        logger.info("============ 上传聚美商城 end !!! ============");
    }

    @Test
    public void testJmMallSku() {
        ShopBean shop = Shops.getShop("028", 27);

        String[] args_jumei_sku_no = {"701299894"};

        for(String jumei_sku_no : args_jumei_sku_no) {
            try {
                StringBuffer sb = new StringBuffer("");
                boolean isSuccess = jumeiHtMallService.updateMallSku(shop, jumei_sku_no, false, sb);

                if (!isSuccess || sb.length() > 0) {
                    // 上传失败
                    throw new BusinessException("更新聚美商城Sku失败!" + sb.toString());
                }
                logger.info(String.format("更新聚美商城Sku成功!jumei_sku_no=%s", jumei_sku_no));
            } catch (BusinessException be) {
                logger.info("jumei_sku_no [" + jumei_sku_no + "] 更新聚美商城Sku失败-1!" + be.getMessage());
            } catch (Exception e) {
                logger.info("jumei_sku_no [" + jumei_sku_no + "] 更新聚美商城Sku失败-2!" + e.getMessage());
            }
        }
    }

    /**
     * 上传到聚美商城
     */
    @Test
    public void testUploadMall() {
//        String channelId = "010";
//        int cartId = 27;
//        ShopBean shop = Shops.getShop(channelId, cartId);
//
//        Long groupId = Long.valueOf("");
//        SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
//        ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
//        CmsBtProductModel product = sxData.getMainProduct();
//        try {
//            cmsBuildPlatformProductUploadJMService.uploadMall(product, shop, expressionParser, null, null);
//        } catch (Exception e) {
//
//        }

        String channelId = "010";
        int cartId = 27;

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        StringBuffer sb = new StringBuffer();
        try {
            jumeiHtMallService.addMall(shopBean, "ht1472723106p810000017", sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试成功上传到聚美商城之后回写状态处理
     */
    @Test
    public void testUpdateMallId() {

        String channelId = "010";
        int cartId = 27;
        String productCode = "B10-416AGDC4-75";
        String mallId = "ID00001";

        try {
            // 获取product信息
            CmsBtProductModel productModel = cmsBtProductDao.selectOneWithQuery("{'common.fields.code':'" + productCode + "'}", channelId);
            if (productModel == null) {
                logger.info("没找到对应的product数据(productCode=" + productCode + ")");
                return;
            }
            // 测试回写状态
            cmsBuildPlatformProductUploadJMService.updateMallId(productModel, mallId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDoHidNotExistSku () {

        String channelId = "028";
        int cartId = 27;
        String productCode = "028-TEAGAN-BLK";
        String mallId = "ID00001";

        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id(channelId);
        shop.setCart_id(String.valueOf(cartId));
        shop.setApp_url("http://openapi.ext.jumei.com/");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shop.setPlatform_id("3");

        try {
            // 获取product信息
            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery("{'common.fields.code':'" + productCode + "'}", channelId);
            if (product == null) {
                System.out.println("没找到对应的product数据(productCode=" + productCode + ")");
                return;
            }

            CmsBtProductModel_Platform_Cart jmCart = product.getPlatform(cartId);
            String originHashId = jmCart.getpNumIId();

            //先去聚美查一下product
            JmGetProductInfoRes jmGetProductInfoRes = jumeiProductService.getProductById(shop, jmCart.getpProductId() );
            List<JmGetProductInfo_Spus> remoteSpus = null;
            if(jmGetProductInfoRes != null)
            {
                remoteSpus = jmGetProductInfoRes.getSpus();
            }
            if(remoteSpus == null)
            {
                remoteSpus = new ArrayList<>();
            }

            //取库存
            Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(StringUtils.isNullOrBlank2(product.getOrgChannelId())? channelId :  product.getOrgChannelId(), jmCart.getSkus().stream().map(w->w.getStringAttribute("skuCode")).collect(Collectors.toList()));

            // 测试
            // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则在平台上隐藏该商品编码并把库存改为0
            cmsBuildPlatformProductUploadJMService.doHideNotExistSkuDeal(shop, originHashId, remoteSpus, product.getPlatform(cartId).getSkus(), skuLogicQtyMap);
            // 如果平台上取得的商家商品编码在mongoDB的产品P27.Skus()中不存在对应的SkuCode，则在聚美商城上隐藏该商品编码并把库存改为0
//        if (!StringUtils.isEmpty(product.getPlatform(CART_ID).getpPlatformMallId()))
            cmsBuildPlatformProductUploadJMService.doHideNotExistSkuMall(shop, remoteSpus, product.getPlatform(cartId).getSkus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试批量修改deal价格处理
     */
    @Test
    public void testUpdateDealPriceBatch() {

        String channelId = "015";
        int cartId = 27;
        String productCode = "1148114032";

        ShopBean shop = Shops.getShop(channelId, cartId);

        try {
            // 获取product信息
            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery("{'common.fields.code':'" + productCode + "'}", channelId);
            if (product == null) {
                logger.info("没找到对应的product数据(productCode=" + productCode + ")");
                return;
            }
            // 测试回写状态
            cmsBuildPlatformProductUploadJMService.updateDealPriceBatch(shop, product, true, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHidByCode() {
        String channelId = "027";
        int cartId = 27;
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String[] codes = {"027-1101-03","027-1101-05","027-1101-06","027-1101-07","027-1101-08","027-1101-09","027-1101-12","027-1101-13","027-1101-14","027-1101-15"};
        List<String> listSku = Lists.newArrayList("027-1101XL03","027-1101XXL03","027-1101XXXL03","027-1101XL05","027-1101XXL05","027-1101XXXL05","027-1101XL06","027-1101XXL06","027-1101XXXL06","027-1101XL07","027-1101XXL07","027-1101XXXL07","027-1101XL08","027-1101XXL08","027-1101XXXL08","027-1101XL09","027-1101XXL09","027-1101XXXL09","027-1101XL12","027-1101XXL12","027-1101XXXL12","027-1101XL13","027-1101XXL13","027-1101XXXL13","027-1101XL14","027-1101XXL14","027-1101XXXL14","027-1101XL15","027-1101XXL15","027-1101XXXL15");

        List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codes, "$in") + "}", channelId);

        for (CmsBtProductModel productModel : productModelList) {
            String originHashId = productModel.getPlatform(cartId).getpNumIId();
            for (BaseMongoMap<String, Object> sku : productModel.getPlatform(cartId).getSkus()) {
                String skuCode = sku.getStringAttribute("skuCode");
                if (!listSku.contains(skuCode)) {
                    continue;
                }

                String jmSkuNo = sku.getStringAttribute("jmSkuNo");
                try {
                    cmsBuildPlatformProductUploadJMService.updateSkuIsEnableDeal(shopBean, originHashId, jmSkuNo, "0");
                    logger.info(String.format("jmSkuNo[%s] Deal 下架成功!", jmSkuNo));
                } catch (Exception e) {
                    logger.error(String.format("jmSkuNo[%s] Deal 下架失败!" + e.getMessage(), jmSkuNo));
                }


                StringBuffer failCause = new StringBuffer("");
                try {
                    cmsBuildPlatformProductUploadJMService.updateSkuIsEnableMall(shopBean, jmSkuNo, "disabled", failCause);
                    if (failCause.length() > 0) {
                        logger.error(String.format("jmSkuNo[%s] Mall 下架失败!" + failCause.toString(), jmSkuNo));
                    } else {
                        logger.info(String.format("jmSkuNo[%s] Mall 下架成功!", jmSkuNo));
                    }
                } catch (Exception e) {
                    logger.error(String.format("jmSkuNo[%s] Mall 下架失败!" + e.getMessage(), jmSkuNo));
                }

            }
        }

    }

    @Test
    public void testSaveProductPlatform() {
        String channelId = "028";
        int cartId = 27;
        String productCode = "028-ps4716508";

        CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, productCode);
        if (cmsBtProductModel == null) {
            System.out.print(String.format("没找到对应的产品信息 [ProductCode:%s]", productCode));
        }

        List<BaseMongoMap<String, Object>> jmSkus = cmsBtProductModel.getPlatform(cartId).getSkus();
        int i = 1;
        for (BaseMongoMap<String, Object> sku : jmSkus) {
            sku.setStringAttribute("sizeNick", "39."+i);
            i++;
        }

        cmsBuildPlatformProductUploadJMService.saveProductPlatform(channelId, cmsBtProductModel);
    }

    @Test
    public void testDoUpdateMallStatus() {
        String channelId = "023";
        int cartId = 27;
        String productCode = "VN-0KC44K1";
        String pPlatformMallId = "23602";

        CmsBtProductGroupModel productGroupModel = productGroupService.selectProductGroupByCode(channelId, productCode, cartId);
        if (productGroupModel == null) {
            System.out.print(String.format("没找到对应的产品Group信息 [ProductCode:%s]", productCode));
        }

        ShopBean shop = Shops.getShop(channelId, cartId);

        // 调用聚美商城商品上下架
        cmsBuildPlatformProductUploadJMService.doUpdateMallStatus(pPlatformMallId, productGroupModel.getPlatformActive(), shop);
    }

    @Test
    public void testUpdateSkuIsEnableDeal() {

        String channelId = "028";
        int cartId = 27;
        String originHashId = "ht1479292676p3174515";
        String jumeiSkuNo = "701418385";

        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id(channelId);
        shop.setCart_id(String.valueOf(cartId));
        shop.setApp_url("http://openapi.ext.jumei.com/");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shop.setPlatform_id("3");

        try {
            // 把聚美平台上deal中的sku下架
            cmsBuildPlatformProductUploadJMService.updateSkuIsEnableDeal(shop, originHashId, jumeiSkuNo, "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddSkuNoToJumeiMall() {
        // 手动追加skuNo到聚美mall中
        // ==================================================
        //  聚美MALL更新时第1批5个sku的聚美商城商品价格更新失败! 批量修改商城商品价格[MALL](/v1/htMall/updateMallPriceBatch)时,
        //      --  发生错误[302:未全部成功,包括全部失败(successCount对应成功的条数, msg对应失败原因, errorList对应失败的jumei_sku_no)]
        //  {"error_code":"302","reason":"error","response":{"successCount":3,"errorList":[
        //  {"jumei_sku_no":701311778,"error_message":"skuNo:701311778不在售卖状态, 请核实!","error_code":"100010"},
        //  {"jumei_sku_no":701311779,"error_message":"skuNo:701311779不在售卖状态, 请核实!","error_code":"100010"}]}}
        // -----------------------------
        // 如果报上面的错误时:
        //  1.先检查jumeiMallId是否存在，不存在就删除mallId,重新approve
        //  2.如果mallId存在，再检查jumeiSpuNo是否存在，不存在就删除该sku的jmSpuNo,重新approve(不存在会报"该jumei_spu_no不存在"错误)
        //  3.再用我们这个UploadJmTest.testAddSkuNoToJumeiMall()追加一个sku到聚美mall
        // ==================================================
        String channelId = "028";
        int CART_ID = 27;
        String productCode = "028-ps6080342";
        StringBuffer sbFault = new StringBuffer();

        ShopBean shopBean = Shops.getShop(channelId, CART_ID);
        if (shopBean == null) {
            System.out.println("没有取到店铺信息");
            return;
        }
//        ShopBean shopBean = new ShopBean();
//        shopBean.setApp_url("https://api.jd.com/routerjson");
//        shopBean.setAppKey("");
//        shopBean.setAppSecret("");
//        shopBean.setSessionKey(""); // 京东国际匠心界全球购专营店(SessionKey)
//        shopBean.setOrder_channel_id(channelId);
//        shopBean.setCart_id(StringUtils.toString(CART_ID));
//        shopBean.setShop_name("京东国际匠心界全球购专营店");

        CmsBtProductModel product = productService.getProductByCode(channelId, productCode);
        if (product == null) {
            System.out.print(String.format("没找到对应的产品信息 [ProductCode:%s]", productCode));
            return;
        }

        List<String> addSkuList = new ArrayList() {{
            add("028-3092447");
        }};

        //取库存
        Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(StringUtils.isNullOrBlank2(product.getOrgChannelId())? channelId :  product.getOrgChannelId(),
                product.getPlatformNotNull(CART_ID).getSkus().stream().map(w->w.getStringAttribute("skuCode")).collect(Collectors.toList()));

        String inserHeader = "INSERT INTO voyageone_cms2.cms_bt_jm_sku (channel_id, product_code, sku_code, jm_spu_no, jm_sku_no, format, upc, cms_size, jm_size, msrp_usd, msrp_rmb, retail_price, sale_price, creater, created, modified, modifier) VALUES(";
        // 追加sku
        if (ListUtils.notNull(addSkuList)) {
            List<BaseMongoMap<String, Object>> skuList = product.getPlatform(CART_ID).getSkus();
            List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
            skuList = cmsBuildPlatformProductUploadJMService.mergeSkuAttr(skuList, commonSkus);
            for (BaseMongoMap<String, Object> sku : skuList) {
                String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                if (addSkuList.contains(skuCode)) {
                    HtMallSkuAddInfo mallSkuAddInfo = new HtMallSkuAddInfo();
                    mallSkuAddInfo.setJumeiSpuNo(sku.getStringAttribute("jmSpuNo"));
                    HtMallSkuAddInfo.SkuInfo skuInfo = mallSkuAddInfo.getSkuInfo();
//                        skuInfo.setCustoms_product_number(" "); // 发货仓库为保税区仓库时，此处必填, 现在暂时不用设置
//                        skuInfo.setCustoms_product_number(skuCode);
                    skuInfo.setBusinessman_num(skuCode);
                    Integer stock = skuLogicQtyMap.get(skuCode);
                    if (stock == null) {
                        stock = 0;
                    }

                    skuInfo.setStocks(stock);
                    skuInfo.setMall_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceSale.name()));
                    skuInfo.setMarket_price(sku.getDoubleAttribute(CmsBtProductConstants.Platform_SKU_COM.priceMsrp.name()));

                    sbFault.setLength(0);
                    String jumeiSkuNo = null;
                    try {
                        jumeiSkuNo = jumeiHtMallService.addMallSku(shopBean, mallSkuAddInfo, sbFault);
                        if (StringUtils.isEmpty(jumeiSkuNo) || sbFault.length() > 0) {
                            // 价格更新失败throw出去
                            // 如果jmSkuNo在平台上不存在会报"商城商品追加sku[MALL](v1/htSku/addMallSku)时,发生错误[100008:该jumei_spu_no不存在]
                            // {"error_code":"100008","reason":"error","response":""}"
                            throw new BusinessException("聚美商城追加sku失败!" + sbFault.toString());
                        }
                    } catch (Exception e) {
                    }

                    // 回写(自己手动回写到mysql表吧)
                    String sizeStr = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.sizeSx.name());
                    CmsBtJmSkuModel cmsBtJmSkuModel = cmsBuildPlatformProductUploadJMService.fillNewCmsBtJmSkuModel(product.getChannelId(), product.getCommon().getFields().getCode(), sku, sizeStr);
                    cmsBtJmSkuModel.setJmSpuNo(sku.getStringAttribute("jmSpuNo"));
                    cmsBtJmSkuModel.setJmSkuNo(jumeiSkuNo);

                    System.out.println(String.format("%s'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','system', now(), now(), 'system');",
                            inserHeader, cmsBtJmSkuModel.getChannelId(), cmsBtJmSkuModel.getProductCode(), cmsBtJmSkuModel.getSkuCode(),
                            cmsBtJmSkuModel.getJmSpuNo(), cmsBtJmSkuModel.getJmSkuNo(), cmsBtJmSkuModel.getFormat(), cmsBtJmSkuModel.getUpc(),
                            cmsBtJmSkuModel.getCmsSize(), cmsBtJmSkuModel.getJmSize(), cmsBtJmSkuModel.getMsrpUsd(), cmsBtJmSkuModel.getMsrpRmb(),
                            cmsBtJmSkuModel.getRetailPrice(), cmsBtJmSkuModel.getSalePrice()));
////                        cmsBtJmSkuDao.insert(cmsBtJmSkuModel);
//                    insertOrUpdateCmsBtJmSku(cmsBtJmSkuModel, product.getChannelId(), product.getCommon().getFields().getCode());
//
//                    sku.setStringAttribute("jmSkuNo", jumeiSkuNo);
//                    saveProductPlatform(product.getChannelId(), product);
                }
            }
        }
    }

    @Test
    public void testSaveBtJmSku() {
        // 回写mysql的cms_bt_jm_sku表的测试

        String channelId = "928";
        long groupId = 503173L;

        SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);

        if (sxData == null) {
            System.out.println("sxData == null");
            return;
        }

        // 如果取得上新对象商品信息出错时，报错
        if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
            System.out.println(sxData.getErrorMessage());
            return;
        }

        // 上新对象产品Code列表
        List<String> listSxCode = sxData.getProductList().stream().map(p -> p.getCommon().getFields().getCode()).collect(Collectors.toList());

        // 调用聚美商城商品上下架
        cmsBuildPlatformProductUploadJMService.saveBtJmSku(channelId, listSxCode, sxData);
        System.out.println("回写mysql的cms_bt_jm_sku表的测试 ok");
    }
    // 测试聚美商城价格更新
    @Test
    public void testUpdateMallPrice() {
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id("928");
        shop.setCart_id(String.valueOf("27"));
        shop.setApp_url("http://openapi.ext.jumei.com/");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        List<HtMallSkuPriceUpdateInfo> updateData = new ArrayList<>();

        HtMallSkuPriceUpdateInfo skuInfo = new HtMallSkuPriceUpdateInfo();
        skuInfo.setJumei_sku_no("701300414");
        skuInfo.setMarket_price(2893.00);
        skuInfo.setMall_price(1827.00);
        updateData.add(skuInfo);
        StringBuffer sbPrice = new StringBuffer("");
        try {
            boolean isSuccess = jumeiHtMallService.updateMallSkuPrice(shop, updateData, sbPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试聚美商城更新
    @Test
    public void testUpdateMall() {
        ShopBean shop = new ShopBean();
        shop.setOrder_channel_id("928");
        shop.setCart_id(String.valueOf("27"));
        shop.setApp_url("http://openapi.ext.jumei.com/");
        shop.setAppKey("");
        shop.setAppSecret("");
        shop.setSessionKey("");
        //按groupId取Product
        SxData sxData = sxProductService.getSxProductDataByGroupId("928", 1042325L);
        try {
//            cmsBuildPlatformProductUploadJMService.uploadMall(sxData.getMainProduct(), shop, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testFillCmsBtJmProductModel() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("928", 10627267L);

        CmsBtJmProductModel cmsBtJmProductModel = new CmsBtJmProductModel();
//        cmsBtJmProductModel = cmsBuildPlatformProductUploadJMService.fillCmsBtJmProductModel(cmsBtJmProductModel, sxData.getMainProduct());

        System.out.println(cmsBtJmProductModel.getImage1());
    }


}