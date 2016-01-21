package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BusinessLogGetRequestTest{

    @Test
    public void selectTest(){
        BusinessLogGetRequest request = new BusinessLogGetRequest();
        request.setProductIds(Arrays.asList(221));
        request.setErrType(String.valueOf(1));
        ApiTestUtils.RunWithRest(request);
    }

    @Test
    public void updateTest(){
        BusinessLogUpdateRequest request = new BusinessLogUpdateRequest();
        request.setSeq(20);
        ApiTestUtils.RunWithRest(request);
    }

}
