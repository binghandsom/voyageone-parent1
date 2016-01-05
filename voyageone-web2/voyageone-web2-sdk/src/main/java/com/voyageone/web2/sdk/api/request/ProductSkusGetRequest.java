package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkusGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * get product sku Request Model
 *
 * /puroduct/sku/selectList
 *
 *  1.传入productId Set来查询
 *  2.传入productCode Set来查询
 *  3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkusGetRequest extends VoApiListRequest<ProductSkusGetResponse> {

	public String getApiURLPath() {
		return "/puroduct/sku/selectList";
	}

	private String channelId;

	/**
	 * productId
	 */
	private Set<Long> productIds = new LinkedHashSet<>();

	/**
	 * Product的Code
	 */
	private Set<String> productCodes = new LinkedHashSet<>();


	public ProductSkusGetRequest() {
	}

	public ProductSkusGetRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productIds, or productCodes", productIds, productCodes);
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