package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.HtProductUpdate_ProductInfo;
import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Reponse.HtSkuAddResponse;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.components.jumei.Request.HtSkuAddRequest;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JumeiHtSkuServiceTest {

    @Autowired
    JumeiHtSkuService service;
    @Test
    public void copyDealTest() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        HtSkuAddRequest request = new HtSkuAddRequest();
        request.setBusinessman_num("");
        request.setCustoms_product_number("");
        request.setJumei_spu_no("222550619");
        request.setDeal_price("");
        request.setMarket_price("");
        request.setStocks("");
        HtSkuAddResponse response = service.add(shopBean, request);
    }
}
