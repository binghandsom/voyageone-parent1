package com.voyageone.web2.sdk.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import com.voyageone.web2.sdk.api.response.PostProductSelectOneResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2015/12/10.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class VoApiDefaultClientTest {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Test
    public void testExceute() {
//        DBObject statusQuery = new BasicDBObject("event", "WonGame");
//        statusQuery.put("playerId", "52307b8fe4b0fc612dea2c6f");
//        DBObject fields = new BasicDBObject("$elemMatch", statusQuery);
//        DBObject query = new BasicDBObject("playerHistories",fields);
//        System.out.println(query.toString());

        PostProductSelectOneRequest requestModel = new PostProductSelectOneRequest("001");
        requestModel.setProductId((long)1);
        PostProductSelectOneResponse response = voApiClient.execute(requestModel);

        System.out.println(response);
        CmsBtProductModel mode = response.getProduct();
        System.out.println(mode);
    }
}
