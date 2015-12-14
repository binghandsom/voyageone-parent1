package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.*;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.junit.Test;

/**
 * Created by DELL on 2015/12/10.
 */
public class ProductGetRequestTest {

    @Test
    public void testProductGetRequest() throws ApiException {
        ProductGetRequest request = new ProductGetRequest();
        request.setProductId(new Long(1));
        request.setFields("aa");
        System.out.println(request.toString());

        VoApiDefaultClientTest clientTest = new VoApiDefaultClientTest();
        clientTest.setApiUrl("http://localhost:8080/rest/puroduct/selectOne.json");
        ProductGetResponse response = clientTest.execute(request);
        System.out.println(response.toString());
    }
}
