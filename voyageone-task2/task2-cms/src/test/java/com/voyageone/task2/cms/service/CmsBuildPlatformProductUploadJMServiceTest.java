package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

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
        work.setChannelId("010");
        work.setGroupId(30222L);
        work.setPublishStatus(0);

        cmsBuildPlatformProductUploadJMService.updateProduct(work);

    }

    @Test
    public void testUpdateProduct2() throws Exception {

        CmsBtSxWorkloadModel workload = new CmsBtSxWorkloadModel();
        workload.setId(185);
        workload.setChannelId("017");
        workload.setCartId(27);
        workload.setGroupId(Long.parseLong("389898"));
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
//        int cartId = 27;
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

}