/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.request;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;

/**
 * @description promotionRequest请求测试
 * @author aooer
 */
public class PromotionRequestTest {

	@Test
	public void testSelect() {
		PromotionsGetRequest request = new PromotionsGetRequest();
		request.setChannelId("001");
		request.setPromotionId(1);
		ApiTestUtils.RunWithRest(request);
	}

	@Test
	public void testPut() {
		PromotionPutRequest request = new PromotionPutRequest();
		CmsBtPromotionModel cmsBtPromotionModel = new CmsBtPromotionModel();
		cmsBtPromotionModel.setPromotionId(31);
		cmsBtPromotionModel.setChannelId("001");
		cmsBtPromotionModel.setCartId(2000);
		cmsBtPromotionModel.setPromotionStatus(true);
		cmsBtPromotionModel.setPromotionName("测试大促");
		request.setCmsBtPromotionModel(cmsBtPromotionModel);
		ApiTestUtils.RunWithRest(request);
	}

	@Test
	public void testDelete() {
		PromotionDeleteRequest request = new PromotionDeleteRequest();
		request.setPromotionId(36);
		ApiTestUtils.RunWithRest(request);
	}

}
