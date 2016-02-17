package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.CategorySchemaGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /categorySchema/selectOne Request Model
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
public class CategorySchemaGetRequest extends VoApiRequest<CategorySchemaGetResponse> {

	public String getApiURLPath() {
		return "/categorySchema/selectOne";
	}

	private String categoryId;


	public CategorySchemaGetRequest() {

	}

	public CategorySchemaGetRequest(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("categoryId", categoryId);
	}

}