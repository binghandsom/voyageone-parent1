package com.voyageone.service.impl.cms.jumei;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.voyageone.BaseTest;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.service.model.jumei.businessmodel.CmsBtJmImportSaveInfo;
import com.voyageone.service.model.jumei.businessmodel.JmProductImportAllInfo;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 * 添加聚美活动的集成测试
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
            "\"id\":1017,\"cmsBtJmMasterBrandId\":0,\"category\":\"\",\"brand\":\"54545\",\"channelId\":\"997\"," +
            "\"activityEnd\":\"2016-04-28 00:00:00\",\"$$hashKey\":\"object:4483\"}";


    @Test
    public void testAddProductionToPromotion() throws Exception {

        List<Long> productIds = ImmutableList.of(5924L, 5925L, 5926L);
        CmsBtJmPromotionModel promotion = new Gson().fromJson(promotionStr, CmsBtJmPromotionModel.class);


        String channelId = "010";
        double discount = 1.0;
        int priceType = 1;
        JmProductImportAllInfo importInfos = jmPromotionService.buildJmProductImportAllInfo(productIds, promotion, channelId, discount, priceType);

        CmsBtJmImportSaveInfo realSaveInfos = jmPromotionImportTaskService.loadListSaveInfo(importInfos, "will");
        Assert.assertTrue(realSaveInfos!=null);
    }
}
