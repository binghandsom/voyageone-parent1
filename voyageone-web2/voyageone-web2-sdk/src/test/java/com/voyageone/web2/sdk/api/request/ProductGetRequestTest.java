package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by DELL on 2015/12/10.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductGetRequestTest {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testProductGetRequest() {


        ProductGetRequest requestModel = new ProductGetRequest("001");
        requestModel.setProductId((long)1);

        RequestEntity<ProductGetRequest> requestEntity = new RequestEntity<ProductGetRequest>(requestModel, requestModel.getHeaders(), requestModel.getHttpMethod(), getURI(requestModel.getApiURLPath()));

        ResponseEntity<ProductGetResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, ProductGetResponse.class);
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                System.out.println(responseEntity.getBody());
                CmsBtProductModel model = responseEntity.getBody().getProduct();
                System.out.println(responseEntity.getBody().getProduct());
            } else {
                System.out.println(responseEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private String baseUri = "http://localhost:8080/rest";

    /**
     * URI 取得
     * @return URI
     */
    public URI getURI(String apiURLPath) {
        return URI.create(baseUri + apiURLPath);
    }
}
