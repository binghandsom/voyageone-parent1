package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductCategoryUpdateResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

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
public class ProductCategoryUpdateRequest extends VoApiRequest<ProductCategoryUpdateResponse> {

	public String getApiURLPath() {
		return "/product/category/switch";
	}

	private String channelId;

	private String categoryId;

	private String categoryPath;

	private Long productId;


	public ProductCategoryUpdateRequest() {

	}

	public ProductCategoryUpdateRequest(String channelId,String categoryId,String categoryPath, Long productId) {
		this.channelId = channelId;
		this.categoryId = categoryId;
		this.categoryPath = categoryPath;
		this.productId = productId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("categoryId categoryPath productId", categoryId,categoryPath,productId);
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}


}