package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.jumei.Bean.JmGetDealInfoReq;
import com.voyageone.components.jumei.Bean.JmGetDealInfoRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiDealServiceTest {

    @Autowired
    JumeiDealService jumeiDealService;

    @Test
    public void testGet() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");

        JmGetDealInfoReq getDealInfoReq =new JmGetDealInfoReq();
        getDealInfoReq.setProductId("1");
        //getDealInfoReq.setFields("product_id,categorys,brand_id,brand_name,name,foreign_language_name");

        JmGetDealInfoRes dealInfo = jumeiDealService.getDealByHashID(shopBean, getDealInfoReq);

        System.out.println(JsonUtil.getJsonString(dealInfo));
    }
}