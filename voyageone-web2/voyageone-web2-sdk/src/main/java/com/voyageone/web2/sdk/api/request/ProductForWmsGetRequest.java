package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductForWmsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * get the product for Wms system
 *
 * /product/getWmsProductsInfo
 *
 *  1.传入code来查询
 *
 * Created on 2016-02-03
 *
 * @author edward.lin
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductForWmsGetRequest extends VoApiRequest<ProductForWmsGetResponse> {

	public String getApiURLPath() {
		return "/product/getWmsProductsInfo";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * code
	 */
	private String code;

	public ProductForWmsGetRequest() {

	}

	public ProductForWmsGetRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {

		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" code", code);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


}