package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.UnicodeUtil;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;
import com.voyageone.components.jumei.reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealEndTimeResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealPriceBatchResponse;
import com.voyageone.components.jumei.reponse.HtDealUpdateResponse;
import com.voyageone.components.jumei.request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealEndTimeRequest;
import com.voyageone.components.jumei.request.HtDealUpdateDealPriceBatchRequest;
import com.voyageone.components.jumei.request.HtDealUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtDealServiceTest {
    String Client_id="110";
    String Client_key="f06e250dd5d30ab9db3e24c362438c69";
    String Sign="6dbc6df2c67634192e01c2311cab4372575eebeb";
    String url="http://openapi.ext.jmrd.com:8823";
    @Autowired
    JumeiHtDealService htDealService;
    @Test
    public void update() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        request.setJumei_hash_id("ht1464949112p222551364");
        HtDealUpdate_DealInfo dealInfo=new HtDealUpdate_DealInfo();
        dealInfo.setUser_purchase_limit(200);
      // dealInfo.setShipping_system_id(2813);
        request.setUpdate_data(dealInfo);
        HtDealUpdateResponse response = htDealService.update(shopBean, request);
        //{"error_code":"505","reason":"error","response":"仓库[0]不存在或者未启用"}
    }
    @Test
    public void updateDealEndTime() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);
        HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
        request.setJumei_hash_id("ht1464949112p222551364");
        request.setEnd_time(DateTimeUtil.addMinutes(new Date(),1));

        // dealInfo.setShipping_system_id(2813);

        HtDealUpdateDealEndTimeResponse response = htDealService.updateDealEndTime(shopBean, request);
        //{"error_code":"505","reason":"error","response":"仓库[0]不存在或者未启用"}
    }
    @Test
    public void  copyDeal() throws Exception {
//        商家名称:
//        aimee2
//        商家ID(Client_id):
//        110
//        商家键值(Client_key):
//        f06e250dd5d30ab9db3e24c362438c69
//        接口签名(Sign):
//        6dbc6df2c67634192e01c2311cab4372575eebeb
//                重置

      //  String url="http://182.138.102.82:8823";
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setJumei_hash_id("ht1460725421p2225513");//ht1464949112p222551364

        request.setStart_time(DateTimeUtil.addMinutes(new Date(),1));
        request.setEnd_time(DateTimeUtil.addMinutes(new Date(),2));

        HtDealCopyDealResponse response = htDealService.copyDeal(shopBean, request);

       String body= UnicodeUtil.decodeUnicode(response.getBody());
    }

    @Test
    public void  updateDealPriceBatch() throws Exception {

        //以下为聚美测试环境的入口聚美测试环境URL：a.new.jumeiglobal.jmrd.com:82user：aimee2ps：123456abc@补充（需要变更Host）hosts：a.new.jumeiglobal.jmrd.com182.138.102.82端口：82
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey(Client_id);
        shopBean.setAppSecret(Sign);
        shopBean.setSessionKey(Client_key);
        shopBean.setApp_url(url);

        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        List<HtDeal_UpdateDealPriceBatch_UpdateData> list=new ArrayList<>();
        HtDeal_UpdateDealPriceBatch_UpdateData updateData=new HtDeal_UpdateDealPriceBatch_UpdateData();
        updateData.setJumei_hash_id("ht1464949112p222551364");
        updateData.setJumei_sku_no("701506467");
        updateData.setMarket_price(400);
        updateData.setDeal_price(200);
        list.add(updateData);
        request.setUpdate_data(list);
        HtDealUpdateDealPriceBatchResponse response = htDealService.updateDealPriceBatch(shopBean, request);
     //   {"error_code":"302","reason":"error","response":{"successCount":0,"errorList":[{"jumei_sku_no":"701506467","error_code":505,"error_message":"hash_id: ht1464949112p222551364, sku_no:701506467的修改价格申请还在审核，不能重复提交申请!"}]}}
    }
    @Test
    public void getTime()  {
        long result=0;
        Date d=new Date();
        Calendar now = Calendar.getInstance();
        // 取得系统时间和格林威治时间之间的偏移值
        int diffsecond = now.getTimeZone().getRawOffset();

        DateTimeUtil.getLocalTime(d,8);
        if(diffsecond == 0){
            result= d.getTime() / 1000 - 8 * 3600;
        }else{
            result= d.getTime() / 1000;
        }
    }
}
