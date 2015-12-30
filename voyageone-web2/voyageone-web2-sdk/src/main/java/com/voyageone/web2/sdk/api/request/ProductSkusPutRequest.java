package com.voyageone.web2.sdk.api.request;


import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkusPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.*;

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
public class ProductSkusPutRequest extends VoApiListRequest<ProductSkusPutResponse> {

	public String getApiURLPath() {
		return "/puroduct/sku/put";
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

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
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