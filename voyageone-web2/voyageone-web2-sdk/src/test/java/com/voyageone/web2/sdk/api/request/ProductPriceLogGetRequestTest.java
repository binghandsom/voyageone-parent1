package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.domain.CmsBtPriceLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProductPriceLogGetRequestTest {

	@Autowired
	protected VoApiDefaultClient voApiClient;

	@Test
	public void testGet() {
		ProductPriceLogGetRequest requestModel = new ProductPriceLogGetRequest("300");
		requestModel.setProductCode("100001");
		requestModel.setOffset(10);
		requestModel.setRows(20);
		//SDK取得Product 数据
		List<CmsBtPriceLogModel> list = voApiClient.execute(requestModel).getPriceList();
		for (CmsBtPriceLogModel log : list) {
			System.out.println(log);
		}
	}
}