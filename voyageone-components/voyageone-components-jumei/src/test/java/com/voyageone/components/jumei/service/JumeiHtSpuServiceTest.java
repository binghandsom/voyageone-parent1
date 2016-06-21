package com.voyageone.components.jumei.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.reponse.HtSpuAddResponse;
import com.voyageone.components.jumei.reponse.HtSpuUpdateResponse;
import com.voyageone.components.jumei.request.HtSpuAddRequest;
import com.voyageone.components.jumei.request.HtSpuUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtSpuServiceTest {

    @Autowired
    private JumeiHtSpuService service;

    @Test
    public void testAddSpu() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
//        shopBean.setApp_url("http://182.138.102.82:8868/");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823");

        HtSpuAddRequest request = new HtSpuAddRequest();
        // 聚美产品ID(必须)
        request.setJumei_product_id("222551454");
        // 商品自带条码(可选)
        request.setUpc_code("113001");
        // 规格(必须): FORMAL 正装 MS 中小样 OTHER 其他
        request.setProperty("OTHER");
        // 容量/尺寸(必须)
        request.setSize("43");
        // 型号/颜色(可选)
        request.setAttribute("C22");
        // 海外官网价(必须)
        request.setAbroad_price(130.50);    // float 小数点后不能只有一个0,必须是2个或2个以上0
        // 货币符号Id(必须)
        request.setArea_code("2223602");     // 2223602:RMB:中国币

        HtSpuAddResponse response = service.add(shopBean, request);
        if (response != null && response.is_Success()) {
            // 聚美添加Spu信息成功
            String reponseBody = response.getBody();
            System.out.println("聚美添加Spu信息成功！ body=" + reponseBody);
        } else {
            // 聚美添加Spu信息失败
            System.out.println("聚美添加Spu信息失败！");
        }
    }

    @Test
    public void testUpdateSpu() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://openapi.ext.jmrd.com:8823");

        HtSpuUpdateRequest request = new HtSpuUpdateRequest();
        // 聚美Spu_No(必须)
        request.setJumei_spu_no("112630");
        // 商品自带条码(可选)
        request.setUpc_code("111021");
        // 规格(可选): FORMAL 正装 MS 中小样 OTHER 其他
        request.setProperty("OTHER");
        // 容量/尺寸(可选)
        request.setSize("45");
        // 型号/颜色(可选)
        request.setAttribute("C31");

        HtSpuUpdateResponse response = service.update(shopBean, request);
        if (response != null && response.is_Success()) {
            // 聚美修改Spu信息成功
            String reponseBody = response.getBody();
            System.out.println("聚美修改Spu信息成功！ body=" + reponseBody);
        } else {
            // 聚美修改Spu信息失败
            System.out.println("聚美修改Spu信息失败！");
        }
    }
}
