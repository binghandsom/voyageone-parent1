package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductsTagPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.*;

/**
 * create product tag Request Model
 *
 * /product/tag/put
 *
 *  传入productId & CmsBtTagModel
 *  Max处理条数:500
 *  存在的作无操作，不存在作添加操作。
 *  记录log
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductsTagPutRequest extends VoApiRequest<ProductsTagPutResponse> {

	public String getApiURLPath() {
		return "/product/tag/put";
	}

	private String channelId;

	/**
	 * productIdTagPathsMap
	 */
	private Map<Long, List<String>> productIdTagPathsMap = new LinkedHashMap<>();

	public ProductsTagPutRequest() {
	}

	public ProductsTagPutRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productIdTagPathsMap", productIdTagPathsMap);
		RequestUtils.checkMinValue((long) productIdTagPathsMap.size(), 1, "productIds");
		RequestUtils.checkMaxValue((long) productIdTagPathsMap.size(), 500, "productIds");
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Map<Long, List<String>> getProductIdTagPathsMap() {
		return productIdTagPathsMap;
	}

	public void setProductIdTagPathsMap(Map<Long, List<String>> productIdTagPathsMap) {
		this.productIdTagPathsMap = productIdTagPathsMap;
	}

	public void addProductIdTagPathsMap(Long productId, String tagPath) {
		if (!productIdTagPathsMap.containsKey(productId)) {
			productIdTagPathsMap.put(productId, new ArrayList<>());
		}
		List<String> tagModelList = productIdTagPathsMap.get(productId);
		if (!tagModelList.contains(tagPath)) {
			tagModelList.add(tagPath);
		}
	}
}