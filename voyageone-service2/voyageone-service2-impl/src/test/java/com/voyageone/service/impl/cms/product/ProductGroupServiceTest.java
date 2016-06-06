package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.common.CmsConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 16/4/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductGroupServiceTest {

    @Autowired
    private ProductGroupService service;

    @Test
    public void testUpdate() throws Exception {
        CmsBtProductGroupModel model = new CmsBtProductGroupModel();
        model.setChannelId("010");
        model.setGroupId(10000L);
        model.setCartId(23);
        model.setNumIId("1234567890");
        model.setPlatformPid("0987654321");
        model.setPublishTime("2016-04-25 11:11:11");
        model.setOnSaleTime("2016-04-25 11:11:12");
        model.setInStockTime("2016-04-25 11:11:13");
        model.setPlatformStatus(CmsConstants.PlatformStatus.InStock);

        List<String> productCodes = new ArrayList<>();
        productCodes.add("51A0HC13E1-00LCNB0");
        productCodes.add("15046");
        model.setProductCodes(productCodes);

        model.setMainProductCode("15046");
        model.setQty(1);

        model.setPriceMsrpSt(12.00);
        model.setPriceMsrpEd(12.01);

        model.setPriceRetailSt(13.00);
        model.setPriceRetailEd(13.01);

        model.setPriceSaleSt(14.00);
        model.setPriceSaleEd(14.01);

        model.setModifier("edward");
        model.setModified("2016-04-25 12:00:00");

        model.setCreater("edward");
        model.setModified("2016-04-25 12:00:00");

        // 插入新group后做更新
        service.insert(model);

        model.setChannelId("010");
        model.setGroupId(10000L);
        model.setCartId(23);
        model.setNumIId("1234567891");
        model.setPlatformPid("1987654321");
        model.setPublishTime("2016-04-25 21:11:11");
        model.setOnSaleTime("2016-04-25 21:11:12");
        model.setInStockTime("2016-04-25 21:11:13");
        model.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
        model.setPlatformActive(CmsConstants.PlatformActive.ToInStock);

        productCodes.add("15053");
        model.setProductCodes(productCodes);

        model.setMainProductCode("15053");
        model.setQty(2);

        model.setPriceMsrpSt(12.10);
        model.setPriceMsrpEd(12.11);

        model.setPriceRetailSt(13.10);
        model.setPriceRetailEd(13.11);

        model.setPriceSaleSt(14.10);
        model.setPriceSaleEd(14.11);

        model.setModifier("edward");

        WriteResult result = service.update(model);
        System.out.println(result.getUpsertedId());
    }

    @Test
    public void testInsert() throws Exception {
        CmsBtProductGroupModel model = new CmsBtProductGroupModel();
        model.setChannelId("010");
        model.setGroupId(10000L);
        model.setCartId(23);
        model.setNumIId("1234567890");
        model.setPlatformPid("0987654321");
        model.setPublishTime("2016-04-25 11:11:11");
        model.setOnSaleTime("2016-04-25 11:11:12");
        model.setInStockTime("2016-04-25 11:11:13");
        model.setPlatformStatus(CmsConstants.PlatformStatus.InStock);

        List<String> productCodes = new ArrayList<>();
        productCodes.add("51A0HC13E1-00LCNB0");
        productCodes.add("15046");
        model.setProductCodes(productCodes);

        model.setMainProductCode("15046");
        model.setQty(1);

        model.setPriceMsrpSt(12.00);
        model.setPriceMsrpEd(12.01);

        model.setPriceRetailSt(13.00);
        model.setPriceRetailEd(13.01);

        model.setPriceSaleSt(14.00);
        model.setPriceSaleEd(14.01);

        model.setModifier("edward");
        model.setModified("2016-04-25 12:00:00");

        model.setCreater("edward");
        model.setModified("2016-04-25 12:00:00");

        WriteResult result = service.insert(model);
        System.out.println(result.getUpsertedId());
    }

    @Test
    public void testUpdateGroupsPlatformStatus() throws Exception {
        CmsBtProductGroupModel model = service.getProductGroupByGroupId("018", 493765L);
        model.setNumIId("1234567890");
        model.setPlatformPid("0987654321");
        model.setPublishTime("2016-04-25 11:11:11");
        model.setOnSaleTime("2016-04-25 11:11:12");
        model.setInStockTime("2016-04-25 11:11:13");
        model.setPlatformStatus(CmsConstants.PlatformStatus.WaitingPublish);

        List<String> productCodes = new ArrayList<>();
        productCodes.add("14218727-ELIXER");
        productCodes.add("14218727-CHROME");
        model.setProductCodes(productCodes);

        model = service.updateGroupsPlatformStatus(model);
        System.out.print(model);
    }

    @Test
    public void testGetProductGroupByGroupId() throws Exception {
        CmsBtProductGroupModel model = service.getProductGroupByGroupId("010", 39795L);

        System.out.print(model);
    }
}