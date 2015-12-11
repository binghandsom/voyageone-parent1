package com.voyageone.web2.sdk.api.response;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.internal.mapping.ApiField;


/**
 * TOP API: voapi.product.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class ProductGetResponse extends VoApiResponse {

	private static final long serialVersionUID = 6532668199498162759L;

	/**
	 * 返回具体信息为入参fields请求的字段信息
	 */
	@ApiField("product")
	private CmsBtProductModel product;

	public void setProduct(CmsBtProductModel product) {
		this.product = product;
	}
	public CmsBtProductModel getProduct( ) {
		return this.product;
	}

}
