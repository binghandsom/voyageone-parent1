package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.cms.SuitSkuInfoForOmsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rex.wu on 2016/12/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SuitSkuInfoForOmsRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testGetSkuBySkuIncludes() {

//        ProductForWmsGetRequest requestModel = new ProductForWmsGetRequest("010");
//        requestModel.setCode("00341");
        SuitSkuInfoForOmsRequest requestModel = new SuitSkuInfoForOmsRequest();
        //SDK取得Product 数据
        SuitSkuInfoForOmsResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
