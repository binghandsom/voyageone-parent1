package com.voyageone.service.impl.cms.jumei;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.voyageone.service.model.cms.CmsBtJmProductImagesModel;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportSaveInfo;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmProductImportSaveInfo;
import com.voyageone.service.bean.cms.businessmodel.JmProductImportAllInfo;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

/**
 * 添加聚美活动的集成测试
 *
 * @description
 * @author: holysky
 * @date: 2016/4/25 10:36
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */

public class CmsBtJmPromotionServiceTest {

    @Resource
    CmsBtJmPromotionService jmPromotionService;

    @Resource
    CmsBtJmPromotionImportTaskService jmPromotionImportTaskService;

    String promotionStr = "{\"prePeriodStart\":\"2016-04-18 00:00:00\",\"created\":\"2016-04-18 13:51:16\",\"isDelete\":false," +
            "\"activityStart\":\"2016-04-12 00:00:00\",\"modifier\":\"will\",\"shippingSystemId\":0," +
            "\"activityPcId\":\"5454\",\"cmsBtJmMasterBrandName\":\"\",\"prePeriodEnd\":\"2016-04-21 00:00:00\"," +
            "\"name\":\"ttrtrtr\",\"activityAppId\":\"5454\",\"creater\":\"will\",\"modified\":\"2016-04-20 19:25:08\"," +
            "\"id\":1017,\"cmsBtJmMasterBrandId\":0,\"category\":\"\",\"brand\":\"54545\",\"channelId\":\"010\"," +
            "\"activityEnd\":\"2016-04-28 00:00:00\",\"$$hashKey\":\"object:4483\"}";



//    @Resource
//    CmsBtTagDao cmsBtJmPromotionImportTaskDao;
//
//    @Test
//    public void testInsert() throws Exception {
//        CmsBtTagModel tag = new CmsBtTagModel();
//        tag.setChannelId("010");
//        tag.setParentTagId(0);
//        tag.setTagName("zhaotianwu");
//        tag.setTagPath("xxx/xxx");
//        tag.setTagPathName("xxx/xxx");
//        tag.setTagType(1);
//        tag.setModifier("zhao");
//        tag.setCreater("zhao");
//        cmsBtJmPromotionImportTaskDao.insertCmsBtTag(tag);
//
//    }

    /**
     * 测试待生成的导入数据是否正确
     * @throws Exception
     */
    @Test
    public void testGenImportData() throws Exception {

        JmProductImportAllInfo importInfos = prepareData();

        //1.image 空  模板未配置 commons-net commons-net-3.3.jar
        //2.sku不对   没导完

        assertTrue("导入数据为空",importInfos.getListProductModel().size()>0);

        CmsBtJmImportSaveInfo realSaveInfos = jmPromotionImportTaskService.loadListSaveInfo(importInfos, "will");
        jmPromotionImportTaskService.saveJmProductImportAllInfo(importInfos,"will");
        assertTrue(realSaveInfos != null);
        List<CmsBtJmProductImportSaveInfo> saveInfos = realSaveInfos.getListProductSaveInfo();
        assertTrue(saveInfos != null && saveInfos.size() > 0);
        IntStream.range(0, saveInfos.size()).forEach((i) -> {
            CmsBtJmProductImportSaveInfo saveInfo = saveInfos.get(i);
            CmsBtJmProductModel product = saveInfo.getProductModel();
            List<CmsBtJmSkuModel> skus = saveInfo.getListSkuModel();
            List<CmsBtJmProductImagesModel> images = saveInfo.getListCmsBtJmProductImagesModel();
            assertTrue(product != null);
            assertTrue(skus.size() > 0);
            assertTrue(images.size() > 0); //error
        });

        //数据case
        Set<String> originProductCodes = importInfos.getListProductModel().stream().map(bean -> bean.getProductCode()).collect(toSet());
        Set<String> newProductCodes = saveInfos.stream().map(bean -> bean.getProductModel().getProductCode()).collect(toSet());
        assertEquals(originProductCodes,newProductCodes);

        Set<String> skuProductCodes = saveInfos.stream().map(bean -> {
            return bean.getListSkuModel().stream().map(sku -> sku.getProductCode()).collect(toList());
        }).flatMap(l->l.stream()).collect(Collectors.toSet());
        assertEquals(originProductCodes,skuProductCodes); //所有的sku中的productCode 必须和原先选定的productCode相等
    }

    @Test
    @Transactional(readOnly = false)
    public void testSaveImportInfos() throws Exception {
        JmProductImportAllInfo importInfos = prepareData();
        jmPromotionImportTaskService.saveJmProductImportAllInfo(importInfos,"will");



    }

    /**
     * 准备数据
     * @return
     */
    private JmProductImportAllInfo prepareData() {
        List<Long> productIds = ImmutableList.of(5924L, 5925L, 5926L);
        CmsBtJmPromotionModel promotion = new Gson().fromJson(promotionStr, CmsBtJmPromotionModel.class);


        String channelId = "010";
        double discount = 1.0;
        int priceType = 1;
       // return jmPromotionService.buildJmProductImportAllInfo(productIds, promotion, channelId, discount, priceType);
        return null;
    }



}
