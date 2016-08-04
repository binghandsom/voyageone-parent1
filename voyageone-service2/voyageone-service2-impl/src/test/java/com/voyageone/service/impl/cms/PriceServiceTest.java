package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Ethan Shi on 2016/7/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PriceServiceTest {

    @Autowired
    PriceService priceService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Test
    public void testSetRetailPrice() throws Exception {
        CmsBtProductModel product = cmsBtProductDao.selectByCode("123879", "017");
        priceService.setRetailPrice(product, 23);
        System.out.println(JsonUtil.bean2Json(product.getPlatform(23).getSkus()));

    }
}