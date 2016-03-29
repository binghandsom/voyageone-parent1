package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.HtDealCopyDeal_DealInfo;
import com.voyageone.common.components.jumei.Bean.HtProductUpdate_ProductInfo;
import com.voyageone.common.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JumeiHtProductServiceTest {
    @Autowired
    JumeiHtProductService service;
    @Test
   public void copyDealTest() throws Exception {
       ShopBean shopBean = new ShopBean();
       shopBean.setAppKey("72");
       shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
       shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
       shopBean.setApp_url("http://182.138.102.82:8868/");
       HtProductUpdateRequest request = new HtProductUpdateRequest();
       request.setJumei_product_id(222550619);
       request.setJumei_product_name("aa");
       HtProductUpdate_ProductInfo productInfo=new HtProductUpdate_ProductInfo();
       request.setUpdate_data(productInfo);

       HtProductUpdateResponse response = service.copyDeal(shopBean, request);
   }
}
