package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkusPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * /product/sku/put Request Model
 *
 *  传入productId 或 productCode
 *  传入addSkus(CmsBtProductModel_Sku)
 *  存在的作更新操作，不存在作新建操作。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkusPutRequest extends VoApiRequest<ProductSkusPutResponse> {

	public String getApiURLPath() {
		return "/product/sku/put";
	}

	private String channelId;

	/**
	 * productId
	 */
	private Long productId;

	/**
	 * Product的Code
	 */
	private String productCode;

	private List<CmsBtProductModel_Sku> skus = new ArrayList<>();

	public ProductSkusPutRequest() {
	}

	public ProductSkusPutRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productId, or productCode", productId, productCode);
		RequestUtils.checkNotEmpty(" skus", skus);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<CmsBtProductModel_Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<CmsBtProductModel_Sku> skus) {
		this.skus = skus;
	}

	public void addSkus(CmsBtProductModel_Sku sku) {
		skus.add(sku);
	}

}