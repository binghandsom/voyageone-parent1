package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.GetDealInfoRes;
import com.voyageone.common.components.jumei.Bean.GetDealInfoReq;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
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
@ContextConfiguration(locations = "classpath:applicationContext.xml")
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

        GetDealInfoReq getDealInfoReq =new GetDealInfoReq();
        getDealInfoReq.setProductId("1");
        //getDealInfoReq.setFields("product_id,categorys,brand_id,brand_name,name,foreign_language_name");

        GetDealInfoRes dealInfo = jumeiDealService.getDealByHashID(shopBean, getDealInfoReq);

        System.out.println(JsonUtil.getJsonString(dealInfo));
    }
}