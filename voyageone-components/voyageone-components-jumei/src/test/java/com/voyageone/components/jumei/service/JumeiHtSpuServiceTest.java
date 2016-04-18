package com.voyageone.components.jumei.service;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.Reponse.HtSpuAddResponse;
import com.voyageone.components.jumei.Request.HtSpuAddRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtSpuServiceTest {
    @Autowired
    JumeiHtSpuService service;
    @Test
    public void addTest() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        HtSpuAddRequest request = new HtSpuAddRequest();
        request.setUpc_code("ewwe");
        request.setJumei_product_id("222550619");

        HtSpuAddResponse response = service.add(shopBean, request);
    }
}
