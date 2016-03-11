/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.junit.Test;

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
