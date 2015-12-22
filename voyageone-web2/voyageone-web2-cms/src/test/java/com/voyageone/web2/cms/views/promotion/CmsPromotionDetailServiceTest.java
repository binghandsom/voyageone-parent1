package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author james.li on 2015/12/16.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionDetailServiceTest {

    @Autowired
    CmsPromotionDetailService cmsPromotionDetailService;

    @Test
    public void testInsertPromotionProduct() throws Exception {

        List< CmsPromotionProductPriceBean > productPrices = new ArrayList<>();
        CmsPromotionProductPriceBean cmsPromotionProductPriceBean = new CmsPromotionProductPriceBean();
        cmsPromotionProductPriceBean.setCode("100004");
        cmsPromotionProductPriceBean.setPrice(20.99);
        cmsPromotionProductPriceBean.setTag("7折");
        productPrices.add(cmsPromotionProductPriceBean);

        cmsPromotionProductPriceBean = new CmsPromotionProductPriceBean();
        cmsPromotionProductPriceBean.setCode("100003");
        cmsPromotionProductPriceBean.setPrice(10.99);
        cmsPromotionProductPriceBean.setTag("7折");
        productPrices.add(cmsPromotionProductPriceBean);

        productPrices.add(cmsPromotionProductPriceBean);
        cmsPromotionDetailService.insertPromotionProduct(productPrices, 15, "james");
        System.out.println("");
    }

    @Test
    public void testGetPromotionGroup() throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("promotionId",1);
        param.put("start",1);
        param.put("length",10);
        cmsPromotionDetailService.getPromotionGroup(param);
    }

    @Test
    public void testResolvePromotionXls() throws Exception {
        InputStream stream = new FileInputStream("d:\\promotion.xlsx");
        cmsPromotionDetailService.resolvePromotionXls(stream);
    }

    @Test
    public void testUploadPromotion() throws Exception {
        InputStream stream = new FileInputStream("d:\\promotion.xlsx");
        cmsPromotionDetailService.uploadPromotion(stream,15,"james");
        System.out.println("");
    }

    @Test
    public void testTeJiaBaoInit() throws Exception {
        cmsPromotionDetailService.teJiaBaoInit(15,"james");
    }
}