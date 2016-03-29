package com.voyageone.task2.cms.service;

import com.voyageone.common.components.jumei.Bean.JmGetProductInfoRes;
import com.voyageone.common.components.jumei.JumeiProductService;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class jumeiServiceTest {
     @Autowired
    JumeiProductService productService;

    @Test
    public void testGet() throws Exception {
        JumeiProductService productService =new JumeiProductService();
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        JmGetProductInfoRes productBean = productService.getProductById(shopBean, "55703");
    }
    @Test
    public void testGet2() throws Exception {
        JumeiProductService productService =new JumeiProductService();
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        JmGetProductInfoRes productBean = productService.getProductByName(shopBean, "Nike SB Stefan Janoski 耐克男子气垫滑板鞋 斑马板鞋642061-651 642061-401");
        System.out.println(JacksonUtil.bean2Json(productBean));
    }
}
