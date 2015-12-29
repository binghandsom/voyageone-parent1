package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;


/**
 * /puroduct/selectList products get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductsGetResponse extends VoApiListResponse {

	/**
	 * 数据体信息
	 */
    private List<CmsBtProductModel> products;

	public List<CmsBtProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<CmsBtProductModel> products) {
		this.products = products;
	}
}
