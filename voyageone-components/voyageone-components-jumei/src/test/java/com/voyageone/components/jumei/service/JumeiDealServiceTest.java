package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.components.jumei.bean.JmGetDealInfoReq;
import com.voyageone.components.jumei.bean.JmGetDealInfoRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;

/**
 * @author aooer 2016/1/28.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiDealServiceTest {

    @Autowired
    private JumeiDealService jumeiDealService;

    @Test
    public void testGetDealByHashID() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823/");

        JmGetDealInfoReq getDealInfoReq =new JmGetDealInfoReq();
        getDealInfoReq.setProductId("222551454");
//        getDealInfoReq.setFields("product_id,categorys,brand_id,brand_name,name,foreign_language_name");
//        getDealInfoReq.setFields("deal_status,product_id,product_short_name,foreign_language_name,sku_list,refund_policy,sku_min_price,sku_max_market_price,with_new_image,long_name,medium_name,short_name,url,real_buyer_number,isDirectMail,brand_id,brand_name,brand_logo_url,countries,start_time,end_time,user_purchase_limit,shipping_system_id,deal_product_properties,search_meta_text_custom,description,description_usage,description_images,category_v3_1,category_v3_2,category_v3_3,category_v3_4,category_name_v1,category_name_v2,category_name_v3,category_name_v4");

        JmGetDealInfoRes dealInfo = jumeiDealService.getDealByHashID(shopBean, getDealInfoReq);

        System.out.println(JsonUtil.getJsonString(dealInfo));
    }

}