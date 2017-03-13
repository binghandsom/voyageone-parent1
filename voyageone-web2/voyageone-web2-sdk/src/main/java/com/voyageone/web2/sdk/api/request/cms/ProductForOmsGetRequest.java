package com.voyageone.web2.sdk.api.request.cms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.cms.ProductForOmsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 * get the product for Oms system
 *
 * /product/getOmsProductsInfo
 *
 *  1.传入skuIncludes/skuList,nameIncludes,descriptionIncludes,cartId来查询
 *
 * Created on 2016-02-03
 *
 * @author edward.lin
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductForOmsGetRequest extends VoApiRequest<ProductForOmsGetResponse> {

	@Override
	public String getApiURLPath() {
		return "/cms/product/getOmsProductsInfo";
	}

	/**
	 * channelId
	 */
	private String channelId;

	private String skuIncludes;

	private String nameIncludes;

	private String descriptionIncludes;

	private String cartId;

	private List<String> skuList;

	public ProductForOmsGetRequest() {

	}

	public ProductForOmsGetRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {

		RequestUtils.checkNotEmpty(" channelId", channelId);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSkuIncludes() {
		return skuIncludes;
	}

	public void setSkuIncludes(String skuIncludes) {
		this.skuIncludes = skuIncludes;
	}

	public String getNameIncludes() {
		return nameIncludes;
	}

	public void setNameIncludes(String nameIncludes) {
		this.nameIncludes = nameIncludes;
	}

	public String getDescriptionIncludes() {
		return descriptionIncludes;
	}

	public void setDescriptionIncludes(String descriptionIncludes) {
		this.descriptionIncludes = descriptionIncludes;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}
}