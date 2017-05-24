package com.voyageone.service.impl.cms.product;

import com.voyageone.service.bean.cms.stock.CartChangedStockBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/5/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class ProductStockServiceTest {

    @Autowired
    private ProductStockService productStockService;
    @Test
    public void updateProductStock() throws Exception {


        List<CartChangedStockBean> stockBeans = new ArrayList<>();
        CartChangedStockBean stockBean1 = new CartChangedStockBean();
        stockBean1.setChannelId("001");
        stockBean1.setCartId(0);
        stockBean1.setItemCode("a0c3jk3");
        stockBean1.setSku("a0c3jk3-l");
        stockBean1.setQty(3);
        stockBeans.add(stockBean1);

        CartChangedStockBean stockBean2 = new CartChangedStockBean();
        stockBean2.setChannelId("018");
        stockBean2.setCartId(23);
        stockBean2.setItemCode("14009927");
        stockBean2.setSku("14009927");
        stockBean2.setQty(3);
        stockBeans.add(stockBean2);
        productStockService.updateProductStock(stockBeans, "test");
    }

}