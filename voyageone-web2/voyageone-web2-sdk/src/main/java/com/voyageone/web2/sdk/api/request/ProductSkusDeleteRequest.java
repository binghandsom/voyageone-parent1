package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductSkusDeleteResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * remove product sku Request Model
 *
 * /product/sku/delete
 *
 *  1.传入productId Set 或 productCode Set
 *  2 productId Set或 productCode Set最多100条
 *  删除多条Product中对应的SKUCODE
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductSkusDeleteRequest extends VoApiRequest<ProductSkusDeleteResponse> {

	public String getApiURLPath() {
		return "/product/sku/deletes";
	}

	private String channelId;

	/**
	 * productId
	 */
	private Map<Long, Set<String>> productIdSkuCodeListMap = new LinkedHashMap<>();

	/**
	 * Product的Code
	 */
	private Map<String, Set<String>> productCodeSkuCodeListMap = new LinkedHashMap<>();

	public ProductSkusDeleteRequest() {
	}

	public ProductSkusDeleteRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);

		RequestUtils.checkNotEmpty(" productIdSkuMap, or productCodeSkuMap", productIdSkuCodeListMap, productCodeSkuCodeListMap);
		if (productIdSkuCodeListMap.size() > 0) {
			RequestUtils.checkMinValue((long) productIdSkuCodeListMap.size(), 1, "productIdSkuCodeListMap");
			RequestUtils.checkMaxValue((long) productIdSkuCodeListMap.size(), 100, "productIdSkuCodeListMap");
		}

		if (productCodeSkuCodeListMap.size() > 0) {
			RequestUtils.checkMinValue((long) productCodeSkuCodeListMap.size(), 1, "productCodeSkuCodeListMap");
			RequestUtils.checkMaxValue((long) productCodeSkuCodeListMap.size(), 100, "productCodeSkuCodeListMap");
		}
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Map<Long, Set<String>> getProductIdSkuCodeListMap() {
		return productIdSkuCodeListMap;
	}

	public void setProductIdSkuCodeListMap(Map<Long, Set<String>> productIdSkuCodeListMap) {
		this.productIdSkuCodeListMap = productIdSkuCodeListMap;
	}

	public Map<String, Set<String>> getProductCodeSkuCodeListMap() {
		return productCodeSkuCodeListMap;
	}

	public void setProductCodeSkuCodeListMap(Map<String, Set<String>> productCodeSkuCodeListMap) {
		this.productCodeSkuCodeListMap = productCodeSkuCodeListMap;
	}

	public void addProductIdSkuCodeList(Long productId, Set<String> skuCodeList) {
		productIdSkuCodeListMap.put(productId, skuCodeList);
	}

	public void addProductCodeSkuCodeList(String productCode, Set<String> skuCodeList) {
		productCodeSkuCodeListMap.put(productCode, skuCodeList);
	}

}