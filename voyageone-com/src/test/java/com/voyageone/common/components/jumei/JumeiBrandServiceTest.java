package com.voyageone.common.components.jumei;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JumeiBrandServiceTest {

    @Autowired
    JumeiBrandService brandService;

    @Test
    public void testInsertCmsBtProduct() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        brandService.initBrands(shopBean);
    }
}
