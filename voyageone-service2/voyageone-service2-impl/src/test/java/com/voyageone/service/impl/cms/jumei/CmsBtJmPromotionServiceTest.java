package com.voyageone.service.impl.cms.jumei;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.jd.open.api.sdk.request.order.OrderGetRequest;
import com.voyageone.BaseTest;
import com.voyageone.service.model.jumei.CmsBtJmProductImagesModel;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.service.model.jumei.CmsBtJmSkuModel;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSaveInfo;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmProductImportSaveInfo;
import com.voyageone.service.model.jumei.businessmodel.JmProductImportAllInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * 添加聚美活动的集成测试
 *
 * @description
 * @author: holysky
 * @date: 2016/4/25 10:36
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */

public class CmsBtJmPromotionServiceTest extends BaseTest {

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


    @Test
    public void testAddProductionToPromotion() throws Exception {

        List<Long> productIds = ImmutableList.of(5924L, 5925L, 5926L);
        CmsBtJmPromotionModel promotion = new Gson().fromJson(promotionStr, CmsBtJmPromotionModel.class);


        String channelId = "010";
        double discount = 1.0;
        int priceType = 1;
        JmProductImportAllInfo importInfos = jmPromotionService.buildJmProductImportAllInfo(productIds, promotion, channelId, discount, priceType);

        //1.image 空  模板未配置
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
}
