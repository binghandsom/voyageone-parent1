package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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

        CmsPromotionProductPriceBean cmsPromotionProductPriceBean = new CmsPromotionProductPriceBean();
        cmsPromotionProductPriceBean.setCode("100001");
        cmsPromotionProductPriceBean.setPrice(20.99);
        cmsPromotionProductPriceBean.setTag("7折");
        List< CmsPromotionProductPriceBean > productPrices = new ArrayList<>();
        productPrices.add(cmsPromotionProductPriceBean);
        cmsPromotionDetailService.insertPromotionProduct(productPrices, "300", 1, 23, "james");
        System.out.println("");
    }
}