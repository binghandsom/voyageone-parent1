package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductsTagPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.*;

/**
 * create product tag Request Model
 *
 * /puroduct/tag/put
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
		return "/puroduct/tag/put";
	}

	private String channelId;

	/**
	 * productIdTagsMap
	 */
	private Map<Long, List<CmsBtTagModel>> productIdTagsMap = new LinkedHashMap<>();

	public ProductsTagPutRequest() {
	}

	public ProductsTagPutRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" productIdTagsMap", productIdTagsMap);
		RequestUtils.checkMinValue((long) productIdTagsMap.size(), 1, "productIds");
		RequestUtils.checkMaxValue((long) productIdTagsMap.size(), 500, "productIds");
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Map<Long, List<CmsBtTagModel>> getProductIdTagsMap() {
		return productIdTagsMap;
	}

	public void setProductIdTagsMap(Map<Long, List<CmsBtTagModel>> productIdTagsMap) {
		this.productIdTagsMap = productIdTagsMap;
	}

	public void addProductIdTag(Long productId, CmsBtTagModel tagModel) {
		if (!productIdTagsMap.containsKey(productId)) {
			productIdTagsMap.put(productId, new ArrayList<>());
		}
		List<CmsBtTagModel> tagModelList = productIdTagsMap.get(productId);
		tagModelList.add(tagModel);
	}
}