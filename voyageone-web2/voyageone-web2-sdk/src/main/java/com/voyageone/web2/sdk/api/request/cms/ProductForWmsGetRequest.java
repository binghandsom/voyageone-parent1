package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.cms.ProductForWmsGetResponse;
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
 * @since 2.0.0
 */
public class ProductForWmsGetRequest extends VoApiRequest<ProductForWmsGetResponse> {

	@Override
	public String getApiURLPath() {
		return "/cms/product/getWmsProductsInfo";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * sku
	 */
	private String sku;

	public ProductForWmsGetRequest() {

	}

	public ProductForWmsGetRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {

		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" sku", sku);
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