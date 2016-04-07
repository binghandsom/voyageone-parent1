package com.voyageone.components.jumei.service;

import com.voyageone.common.components.jumei.Bean.JmGetProductInfoRes;
import com.voyageone.common.components.jumei.Bean.JmProductBean;
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
public class JumeiProductServiceNewTest {

    @Autowired
    JumeiProductService productService;

    @Test
    public void testGet() throws Exception {
       // JumeiProductService productService =new JumeiProductService();
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        JmGetProductInfoRes productBean = productService.getProductById(shopBean, "55703");
    }
    @Test
    public  void  testAddProductAndDeal() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        JmProductBean product = new JmProductBean();
//        {
//            "category_v3_4_id": "V3版四级分类",
//                "brand_id": "品牌id",
//                "name": "产品名(varchar(1000))",
//                "foreign_language_name": "外文名(varchar(255))",
//                "function_ids": "产品功效ID，多个ID用 ","隔开",
//                "normalImage": "白底方图(加前缀的图片,第一张必填,最多10张)(1000*1000格式gif,png,jpg,jpeg,单张不超过1m，多张图片以","隔开)",
//                "verticalImage": "竖图(加前缀的图片,可不传,最多10张)(750*1000格式gif,png,jpg,jpeg,单张不超过1m，多张图片以","隔开)",
//                "diaoxingImage": "调性图片(加前缀的图片,传一张,可不传，单张不超过1m)(1500*400)"
//        }
       // product.setCategory_v3_4_id(21);
        //product.setBrand_id();
        JmGetProductInfoRes productBean = productService.productNewUpload(shopBean,product);
    }
//    @Test
//    public void testGet2() throws Exception {
//      //  JumeiProductService productService =new JumeiProductService();
//        ShopBean shopBean = new ShopBean();
//        shopBean.setAppKey("131");
//        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
//        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
//        shopBean.setApp_url("http://openapi.ext.jumei.com/");
//        JmGetProductInfoRes productBean = productService.getProductByName(shopBean, "Nike SB Stefan Janoski 耐克男子气垫滑板鞋 斑马板鞋642061-651 642061-401");
//        System.out.println(JacksonUtil.bean2Json(productBean));
//    }
}
