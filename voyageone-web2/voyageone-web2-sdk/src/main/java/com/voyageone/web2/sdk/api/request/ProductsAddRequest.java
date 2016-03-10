package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductsAddResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * /product/add product add Request Model
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductsAddRequest extends VoApiRequest<ProductsAddResponse> {

	public String getApiURLPath() {
		return "/product/add";
	}

	private String channelId;

	private List<CmsBtProductModel> products = new ArrayList<>();

	public ProductsAddRequest() {
	}

	public ProductsAddRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" products", products);

		Set<Long> prodIdSet = new HashSet<>();
		Set<String> productCodeSet = new HashSet<>();
		for (CmsBtProductModel product : products) {
			RequestUtils.checkNotEmpty(" prodId", product.getProdId());
			RequestUtils.checkNotEmpty(" fields.Code", product.getFields().getCode());
			prodIdSet.add(product.getProdId());
			productCodeSet.add(product.getFields().getCode());
		}

		String ERROR_CODE_ARGUMENTS_INVALID = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70006.getErrorCode();
		if (prodIdSet.size() != products.size()) {
			throw new ApiException(ERROR_CODE_ARGUMENTS_INVALID, "prodId repeat!");
		}
		if (productCodeSet.size() != products.size()) {
			throw new ApiException(ERROR_CODE_ARGUMENTS_INVALID, "fields.Code repeat!");
		}

		RequestUtils.checkMinValue((long) products.size(), 1, "products");
		RequestUtils.checkMaxValue((long) products.size(), 100, "products");

		RequestUtils.checkNotEmpty(" modifier", modifier);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<CmsBtProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<CmsBtProductModel> products) {
		this.products = products;
	}

	public void addProduct(CmsBtProductModel productModel) {
		products.add(productModel);
	}
}