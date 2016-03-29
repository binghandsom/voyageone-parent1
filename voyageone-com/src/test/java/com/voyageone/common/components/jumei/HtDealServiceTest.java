package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Request.HtDealUpdateRequest;
import com.voyageone.common.components.jumei.Reponse.HtDealUpdateResponse;
import com.voyageone.common.components.jumei.Bean.HtDealUpdate_DealInfo;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class HtDealServiceTest {
    @Autowired
    JumeiHtDealService htDealService;
    @Test
    public void update() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        request.setJumei_hash_id("ht1454411913p2225506");
        HtDealUpdate_DealInfo dealInfo=new HtDealUpdate_DealInfo();
        dealInfo.setUser_purchase_limit(10);
        request.setUpdate_data(dealInfo);
        HtDealUpdateResponse response = htDealService.update(shopBean, request);
    }
}
