package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Field_Image;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.response.ProductStatusPutResponse;
import com.voyageone.web2.sdk.api.response.ProductsAddResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2015/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductStatusPutRequestTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testUpdateStatus() {
        ProductStatusPutRequest requestModel = new ProductStatusPutRequest("001");
        requestModel.setProductId(1L);
        requestModel.setStatus(CmsConstants.ProductStatus.Approved);

        requestModel.setModifier("testLiang");

        //SDK取得Product 数据
        ProductStatusPutResponse response = voApiClient.execute(requestModel);
        System.out.println(response);
    }
}
