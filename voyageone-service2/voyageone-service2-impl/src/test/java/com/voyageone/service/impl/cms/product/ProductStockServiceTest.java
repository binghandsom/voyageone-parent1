package com.voyageone.service.impl.cms.product;

import com.voyageone.common.util.JacksonUtil;
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


//        List<CartChangedStockBean> stockBeans = new ArrayList<>();
//        CartChangedStockBean stockBean1 = new CartChangedStockBean();
//        stockBean1.setChannelId("001");
//        stockBean1.setCartId(26);
//        stockBean1.setItemCode("t501003");
//        stockBean1.setSku("t501003-onesize");
//        stockBean1.setQty(0);
//        stockBeans.add(stockBean1);

        String json = "{\"cartChangedStocks\":[{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-l\",\"itemCode\":\"1280734-025\",\"qty\":0},{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-m\",\"itemCode\":\"1280734-025\",\"qty\":0},{\"channelId\":\"001\",\"cartId\":23,\"sku\":\"1280734-025-s\",\"itemCode\":\"1280734-025\",\"qty\":0}]}";
        List<CartChangedStockBean> stockBeans = JacksonUtil.jsonToBeanList(json,CartChangedStockBean.class);

        productStockService.updateProductStock(stockBeans, "test");
    }

}