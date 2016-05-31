package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealEndTimeResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealEndTimeRequest;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
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
    private JumeiHtDealService htDealService;

    @Test
    public void testUpdateDeal() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823/");
        HtDealUpdateRequest request = new HtDealUpdateRequest();
//        request.setJumei_hash_id("ht1454411913p2225506");
        request.setJumei_hash_id("ht1463986241p222551454");

        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        dealInfo.setUser_purchase_limit("20");
        dealInfo.setProduct_long_name("更新后长名字(李测试)");
        request.setUpdate_data(dealInfo);
        HtDealUpdateResponse response = htDealService.update(shopBean, request);
        if (response != null && response.getIs_Success()) {
            // 更新商品成功
            String reponseBody = response.getBody();
            System.out.println("更新聚美Deal(特卖)信息成功！ body=" + reponseBody);
        } else {
            // 更新商品失败
            System.out.println("更新聚美Deal(特卖)信息失败！");
        }
    }

    @Test
    public void testCopyDeal() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823/");

        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setJumei_hash_id("ht1463986241p222551454");
        request.setStart_time(new Date().getTime());
        request.setEnd_time(new Date().getTime() + 100000000);
        HtDealCopyDealResponse response = htDealService.copyDeal(shopBean, request);
        if (response != null && response.is_Success()) {
            // 更新Deal(特卖)成功
            String reponseBody = response.getBody();
            System.out.println("复制聚美Deal(特卖)信息成功！ body=" + reponseBody);
        } else {
            // 更新Deal(特卖)失败
            System.out.println("复制聚美Deal(特卖)信息失败！");
        }
    }

    @Test
    public void testUpdateDealEndTime() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823/");

        HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
        request.setJumei_hash_id("ht1463986241p222551454");
        request.setEnd_time(new Date().getTime() + 100000000);

        HtDealUpdateDealEndTimeResponse response = htDealService.updateDealEndTime(shopBean, request);
        if (response != null && response.is_Success()) {
            // 延迟聚美Deal结束时间成功
            String reponseBody = response.getBody();
            System.out.println("延迟聚美Deal结束时间成功！ body=" + reponseBody);
        } else {
            // 延迟聚美Deal结束时间失败
            System.out.println("延迟聚美Deal结束时间失败！");
        }
    }

}
