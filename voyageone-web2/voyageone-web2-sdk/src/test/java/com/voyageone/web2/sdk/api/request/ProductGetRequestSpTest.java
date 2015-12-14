package com.voyageone.web2.sdk.api.request;

import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import sun.reflect.annotation.ExceptionProxy;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 2015/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductGetRequestSpTest {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testProductGetRequest() {
        String baseUri = "http://localhost:8080/rest";

        HttpHeaders headers = new HttpHeaders();
        //RequestEntity<CmsBtProductModel> requestEntity = new RequestEntity<CmsBtProductModel>(headers, HttpMethod.POST, URI.create(baseUri + "/puroduct/selectOne"));

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "1");

        RequestEntity requestEntity = new RequestEntity(params, HttpMethod.POST, URI.create(baseUri + "/puroduct/selectOne"));

        ResponseEntity<CmsBtProductModel> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(requestEntity, CmsBtProductModel.class);
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                System.out.println(responseEntity.getBody());
            } else {
                System.out.println(responseEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
