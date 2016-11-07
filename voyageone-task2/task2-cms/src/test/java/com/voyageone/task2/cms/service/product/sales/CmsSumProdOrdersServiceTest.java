package com.voyageone.task2.cms.service.product.sales;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.ListUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.voyageone.common.util.DateTimeUtil.DEFAULT_DATETIME_FORMAT;
import static org.junit.Assert.*;

/**
 * 商品销量统计单元测试
 * Created by jonas on 2016/11/7.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSumProdOrdersServiceTest {
    @Autowired
    private CmsSumProdOrdersService cmsSumProdOrdersService;
    @Autowired
    private ProductService productService;

    /**
     * @since 2.9.0
     */
    @Test
    public void testSumProdOrders() throws Exception {

        long productId = 6362;
        String channelId = "010";
        String taskName = "CmsSumProdOrdersServiceTest";

        String endDate = "2016-05-05 00:00:00";
        DateTime endDateTime = DateTimeFormat.forPattern(DEFAULT_DATETIME_FORMAT).parseDateTime(endDate);

        String begDate1 = endDateTime.minusDays(7).toString(DEFAULT_DATETIME_FORMAT);
        String begDate2 = endDateTime.minusDays(30).toString(DEFAULT_DATETIME_FORMAT);

        CmsBtProductModel cmsBtProductModel = productService.getProductById(channelId, productId);

        List<CmsBtProductModel> prodList = Collections.singletonList(cmsBtProductModel);

        cmsSumProdOrdersService.sumProdOrders(prodList, channelId, begDate1, begDate2, endDate, taskName);
    }
}