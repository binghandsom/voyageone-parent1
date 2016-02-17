package com.voyageone.web2.sdk.api.request;


import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductUpdateResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /product/update product update Request Model
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductUpdateRequest extends VoApiRequest<ProductUpdateResponse> {

	public String getApiURLPath() {
		return "/product/update";
	}

	private String channelId;

	private CmsBtProductModel productModel = null;

	private Boolean isCheckModifed = true;

	public ProductUpdateRequest() {
	}

	public ProductUpdateRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productModel", productModel);
		if (productModel.getFields() != null) {
			RequestUtils.checkNotEmpty(" prodId or fields.code", productModel.getProdId(), productModel.getFields().getCode());
		} else {
			RequestUtils.checkNotEmpty(" prodId", productModel.getProdId());
		}
		RequestUtils.checkNotEmpty(" modifier", modifier);
		if (isCheckModifed) {
			RequestUtils.checkNotEmpty(" productModel.modified", productModel.getModified());
		}
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public CmsBtProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(CmsBtProductModel productModel) {
		this.productModel = productModel;
	}

	public Boolean getIsCheckModifed() {
		return isCheckModifed;
	}

	public void setIsCheckModifed(Boolean isCheckModifed) {
		this.isCheckModifed = isCheckModifed;
	}
}