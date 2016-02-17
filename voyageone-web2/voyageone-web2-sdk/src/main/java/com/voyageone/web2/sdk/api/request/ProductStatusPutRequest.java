package com.voyageone.web2.sdk.api.request;


import com.voyageone.cms.CmsConstants;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductStatusPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;


/**
 * /product/status/update Request Model
 *
 *  传入productId 或 productCode
 *  status
 *  存在的作更新状态操作。
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductStatusPutRequest extends VoApiRequest<ProductStatusPutResponse> {

	public String getApiURLPath() {
		return "/product/status/update";
	}

	private String channelId;

	/**
	 * productId
	 */
	private Long productId;

	/**
	 * Product的Code
	 */
	private String productCode;

	/**
	 * status
	 */
	private CmsConstants.ProductStatus status;

	public ProductStatusPutRequest() {

	}

	public ProductStatusPutRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productId or productCode", productId, productCode);
		RequestUtils.checkNotEmpty(" status", status);
	}

	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public CmsConstants.ProductStatus getStatus() {
		return status;
	}

	public void setStatus(CmsConstants.ProductStatus status) {
		this.status = status;
	}
}