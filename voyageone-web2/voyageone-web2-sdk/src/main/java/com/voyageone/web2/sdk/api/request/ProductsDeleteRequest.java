package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductsDeleteResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * /product/delete Request Model
 *
 * Product的id.两种方式来delete:
 *  1.传入productId List来delete
 *  2.传入productCode list来delete
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductsDeleteRequest extends VoApiRequest<ProductsDeleteResponse> {

	public String getApiURLPath() {
		return "/product/delete";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * productId
	 */
	private Set<Long> productIds = new LinkedHashSet<>();

	/**
	 * Product的Code
	 */
	private Set<String> productCodes = new LinkedHashSet<>();

	public ProductsDeleteRequest() {}

	public ProductsDeleteRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productIdList or productCodeList", productIds, productCodes);
		if (productIds != null && productIds.size() > 0) {
			RequestUtils.checkMinValue((long) productIds.size(), 1, "products");
			RequestUtils.checkMaxValue((long) productIds.size(), 100, "products");
		}
		if (productCodes != null && productCodes.size() > 0) {
			RequestUtils.checkMinValue((long) productCodes.size(), 1, "productCodes");
			RequestUtils.checkMaxValue((long) productCodes.size(), 100, "productCodes");
		}
		RequestUtils.checkNotEmpty(" modifier", modifier);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Set<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<Long> productIds) {
		this.productIds = productIds;
	}

	public Set<String> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(Set<String> productCodes) {
		this.productCodes = productCodes;
	}

	public void addProductId(Long productId) {
		productIds.add(productId);
	}

	public void addProductCode(String productCode) {
		productCodes.add(productCode);
	}

}