package com.voyageone.service.impl.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogFlatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.priceSale;

/**
 * 进行价格比较，并创建日志。并提供查询
 * Created by jonas on 2016/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtPriceLogServiceTest4Mongo {
    @Autowired
    private CmsBtPriceLogService priceLogService;
    @Autowired
    private ProductService productService;

    @Test
    public void testAddLogListAndCallSyncPriceJob() throws Exception {
        List<CmsBtPriceLogFlatModel> paramList = new ArrayList<>();
        CmsBtPriceLogFlatModel model1 = new CmsBtPriceLogFlatModel();
        model1.setChannelId("928");
        model1.setProductId(3316458L);
        model1.setCartId(23);
        model1.setCode("1118913912-2");
        model1.setSku("5089415");
        model1.setModifier("CmsBtPriceLogServiceTest4Mongo");
        model1.setCreater("CmsBtPriceLogServiceTest4Mongo");
        Date ts = new Date();
        model1.setCreated(ts);
        model1.setModified(ts);

        model1.setMsrpPrice(210d);
        model1.setRetailPrice(111d);
        model1.setSalePrice(112d);
        model1.setClientMsrpPrice(113d);
        model1.setClientRetailPrice(114d);
        model1.setClientNetPrice(115d);
        model1.setComment("service添加价格日志测试");

        paramList.add(model1);
        int cnt = priceLogService.addLogListAndCallSyncPriceJob(paramList);
        Assert.assertEquals(1, cnt);
    }

    @Test
    public void testAddLogAndCallSyncPriceJob() throws Exception {

        CmsBtProductModel productModel = productService.getProductByCode("018", "14665398");
        priceLogService.addLogAndCallSyncPriceJob("018", productModel, "检查测试111", "CmsBtPriceLogServiceTest4Mongo");

    }

    @Test
    public void testAddLogForSkuListAndCallSyncPriceJob() throws Exception {
        List<String> skuList = new ArrayList<>();
        skuList.add("14009927");
        priceLogService.addLogForSkuListAndCallSyncPriceJob(skuList, "018", 111L, 23, "CmsBtPriceLogServiceTest4Mongo", "testAddLogForSkuListAndCallSyncPriceJob");
    }
}