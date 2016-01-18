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
 * @author gbb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PromotionRequestTest {

	@Autowired
	VoApiDefaultClient voApiClient;

	@Autowired
	RestTemplate restTemplate;

	private String baseUri = "http://localhost:8080/rest";

	@Test
	public void testSelect() {
		PromotionsGetRequest request = new PromotionsGetRequest();
		request.setChannelId("001");
		request.setPromotionId(1);
		processRequest(request, PromotionsGetResponse.class);
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

		processRequest(request, PromotionsPutResponse.class);
	}

	@Test
	public void testDelete() {
		PromotionDeleteRequest request = new PromotionDeleteRequest();
		request.setPromotionId(36);

		processRequest(request, PromotionsPutResponse.class);
	}

	/**
	 * URI 取得
	 * 
	 * @return URI
	 */
	private URI getURI(String apiURLPath) {
		return URI.create(baseUri + apiURLPath);
	}

	/**
	 * Request 处理方法
	 * 
	 * @param <E> 实际类型
	 *
	 * 
	 * @param request 请求体
	 * @param clazz 响应类
	 */
	private <E> void processRequest(VoApiRequest request, Class<E> clazz) {
		RequestEntity<VoApiRequest> requestEntity = new RequestEntity<VoApiRequest>(
				request, request.getHeaders(), request.getHttpMethod(),
				getURI(request.getApiURLPath()));
		ResponseEntity<E> responseEntity;
		try {
			responseEntity = restTemplate.exchange(requestEntity, clazz);
			System.out.println(responseEntity);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
