package com.voyageone.batch.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.batch.cms.bean.ProductBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 更新商品的单元测试
 * Created by Jonas on 11/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgWsdlUpdateTest {
    @Autowired
    private BcbgWsdlUpdate bcbgWsdlUpdate;

    @Before
    public void setUp() throws Exception {
        BcbgWsdlConstants.init();
    }

    @Test
    public void testPostUpdatedProduct() throws Exception {
        bcbgWsdlUpdate.postUpdatedProduct();
    }

    @Test
    public void testPrice() {
        ProductBean productBean = new ProductBean();
        productBean.setP_msrp("123.789");
        productBean.setPs_price("100");
        productBean.setP_product_type("Apparel");

        bcbgWsdlUpdate.calePrice(productBean);

        System.out.println(new Gson().toJson(productBean));
    }
}