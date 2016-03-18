package com.voyageone.web2.sdk.api.request;

import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductUpdatePriceResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * /product/sku/updatePrice Request
 *
 * 1.addProductPrices(ProductPriceModel) add model
 * 2.max ProductPriceModel 100
 * 3.ProductPriceModel productId or productCode not null
 *
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductUpdatePriceRequest extends VoApiRequest<ProductUpdatePriceResponse> {

	public String getApiURLPath() {
		return "/product/sku/updatePrices";
	}

	/**
	 * channelId
	 */
	private String channelId;

	private List<ProductPriceBean> productPrices = new ArrayList<>();

	public ProductUpdatePriceRequest() {}

	public ProductUpdatePriceRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productPrices ", productPrices);
		for (ProductPriceBean model : productPrices) {
			RequestUtils.checkNotEmpty(" ProductPriceModel ", model);
			RequestUtils.checkNotEmpty(" ProductPriceModel.productId or productCode", model.getProductId(), model.getProductCode());
		}

		RequestUtils.checkMinValue((long) productPrices.size(), 1, "productPrices");
		RequestUtils.checkMaxValue((long) productPrices.size(), 100, "productPrices");
		RequestUtils.checkNotEmpty(" modifier", modifier);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<ProductPriceBean> getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(List<ProductPriceBean> productPrices) {
		this.productPrices = productPrices;
	}

	public void addProductPrices(ProductPriceBean model) {
		productPrices.add(model);
	}

}