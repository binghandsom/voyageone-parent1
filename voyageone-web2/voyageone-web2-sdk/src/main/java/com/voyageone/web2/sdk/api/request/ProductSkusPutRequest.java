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
	private Map<Long, List<CmsBtProductModel_Sku>> productIdSkuMap = new LinkedHashMap<>();

	/**
	 * Product的Code
	 */
	private Map<String, List<CmsBtProductModel_Sku>> productCodeSkuMap = new LinkedHashMap<>();


	public ProductSkusPutRequest() {
	}

	public ProductSkusPutRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" productIdSkuMap, or productCodeSkuMap", productIdSkuMap, productCodeSkuMap);
		if (productIdSkuMap.size() > 0) {
			RequestUtils.checkMinValue((long) productIdSkuMap.size(), 1, "productIdSkuMap");
			RequestUtils.checkMaxValue((long) productIdSkuMap.size(), 100, "productIdSkuMap");
		}
		if (productCodeSkuMap.size() > 0) {
			RequestUtils.checkMinValue((long) productCodeSkuMap.size(), 1, "productIdSkuMap");
			RequestUtils.checkMaxValue((long) productCodeSkuMap.size(), 100, "productIdSkuMap");
		}
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Map<Long, List<CmsBtProductModel_Sku>> getProductIdSkuMap() {
		return productIdSkuMap;
	}

	public void setProductIdSkuMap(Map<Long, List<CmsBtProductModel_Sku>> productIdSkuMap) {
		this.productIdSkuMap = productIdSkuMap;
	}

	public Map<String, List<CmsBtProductModel_Sku>> getProductCodeSkuMap() {
		return productCodeSkuMap;
	}

	public void setProductCodeSkuMap(Map<String, List<CmsBtProductModel_Sku>> productCodeSkuMap) {
		this.productCodeSkuMap = productCodeSkuMap;
	}

	public void addProductId(Long productId, List<CmsBtProductModel_Sku> skus) {
		productIdSkuMap.put(productId, skus);
	}

	public void addProductCode(String productCode, List<CmsBtProductModel_Sku> skus) {
		productCodeSkuMap.put(productCode, skus);
	}

}