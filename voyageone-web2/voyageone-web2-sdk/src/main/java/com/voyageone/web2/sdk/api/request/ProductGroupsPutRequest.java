package com.voyageone.web2.sdk.api.request;


import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGroupsPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * /product/group/put Request Model
 *
 *  传入productId 或 productCode
 *  传入platform
 *  存在的作更新操作，不存在作新建操作。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupsPutRequest extends VoApiRequest<ProductGroupsPutResponse> {

	public String getApiURLPath() {
		return "/product/group/put";
	}

	private String channelId;

	//定义内部class
	/**
	 * productId
	 */
	private Set<Long> productIds = new LinkedHashSet<>();

	/**
	 * Product的Code
	 */
	private Set<String> productCodes = new LinkedHashSet<>();

	private CmsBtProductModel_Group_Platform platform;

	public ProductGroupsPutRequest() {
	}

	public ProductGroupsPutRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" platform", platform);
		RequestUtils.checkNotEmpty(" productIds or productCodes", productIds, productCodes);
		if (productIds != null && productIds.size()>0) {
			RequestUtils.checkMinValue((long) productIds.size(), 1, "productIds");
			RequestUtils.checkMaxValue((long) productIds.size(), 100, "productIds");
		}
		if (productCodes != null && productCodes.size()>0) {
			RequestUtils.checkMinValue((long) productCodes.size(), 1, "productCodes");
			RequestUtils.checkMaxValue((long) productCodes.size(), 100, "productCodes");
		}
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

	public CmsBtProductModel_Group_Platform getPlatform() {
		return platform;
	}

	public void setPlatform(CmsBtProductModel_Group_Platform platform) {
		this.platform = platform;
	}

	public void addProductId(Long productId) {
		productIds.add(productId);
	}

	public void addProductCode(String productCode) {
		productCodes.add(productCode);
	}
}