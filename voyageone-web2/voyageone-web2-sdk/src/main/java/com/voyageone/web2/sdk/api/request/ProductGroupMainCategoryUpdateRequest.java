package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGroupMainCategoryUpdateResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 * /categorySchema/change Request Model
 *
 * category的id查找 schema:
 *  1.传入categoryId来查询
 *
 * Created on 2015-1-28
 *
 * @author lewis
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupMainCategoryUpdateRequest extends VoApiRequest<ProductGroupMainCategoryUpdateResponse> {

	public String getApiURLPath() {
		return "/product/category/switch";
	}

	private String channelId;

	private String categoryId;

	private String categoryPath;

	private List<String> models;


	public ProductGroupMainCategoryUpdateRequest() {

	}

	public ProductGroupMainCategoryUpdateRequest(String channelId, String categoryId, String categoryPath, List<String> models) {
		this.channelId = channelId;
		this.categoryId = categoryId;
		this.categoryPath = categoryPath;
		this.models = models;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId", channelId);
		RequestUtils.checkNotEmpty("categoryId categoryPath models", categoryId,categoryPath,models);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	public List<String> getModels() {
		return models;
	}

	public void setModels(List<String> models) {
		this.models = models;
	}


}