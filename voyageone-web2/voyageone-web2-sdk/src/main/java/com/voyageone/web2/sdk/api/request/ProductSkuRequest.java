package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkuResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /product/selectOne Request Model
 *
 * Product的id.两种方式来查看一个产品:
 *  1.传入productId来查询
 *  2.传入productCode来查询
 *  3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
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