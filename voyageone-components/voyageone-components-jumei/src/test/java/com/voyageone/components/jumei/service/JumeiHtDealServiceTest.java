package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.bean.HtDealCopyDeal_DealInfo;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.Reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.Request.HtDealUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtDealServiceTest {
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
    @Test
    public void  copyDeal() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setJumei_hash_id("ht1454411913p2225506");
        request.setStart_time(new Date().getTime());
        request.setEnd_time(new Date().getTime()+100000000);
        HtDealCopyDealResponse response = htDealService.copyDeal(shopBean, request);
    }

}
