package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsMtPlatformSpecialFieldModel;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.PlatformSpecialFieldsPutResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PlatformSpecialFieldsPutRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testPut() {

        PlatformSpecialFieldsPutRequest requestModel = new PlatformSpecialFieldsPutRequest();

        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(1);
        model.setCatId("11");
        model.setFieldId("aa");
        model.setType("1");
        model.setCreater("liang1");
        model.setModifier("liang2");

        requestModel.addSpecialField(model);

        model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(1);
        model.setCatId("12");
        model.setFieldId("aa22");
        model.setType("2");
        model.setCreater("liang1");
        model.setModifier("liang2");

        requestModel.addSpecialField(model);


        //SDK取得Product 数据
        PlatformSpecialFieldsPutResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
    }
}
