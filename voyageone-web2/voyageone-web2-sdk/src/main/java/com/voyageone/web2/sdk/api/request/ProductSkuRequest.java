package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkuResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /product/getProductSkuQty Request Model
 *
 * Created on 2015-12-14
 *
 */
public class ProductSkuRequest extends VoApiRequest<ProductSkuResponse> {

	public String getApiURLPath() {
		return "/product/getProductSkuQty";
	}

	private String channelId;

	/**
	 * Product的Code
	 */
	private String code;

	private String sku;

	public ProductSkuRequest() {

	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId code", channelId, code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}