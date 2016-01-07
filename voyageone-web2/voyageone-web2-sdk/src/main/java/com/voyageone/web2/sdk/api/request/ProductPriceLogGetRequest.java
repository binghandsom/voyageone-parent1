package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductPriceLogGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * /product/priceLog/get Request Model
 *
 * Product的id.两种方式来查看一个产品:
 *  1.传入productId List来查询
 *  2.传入productCode list来查询
 *  3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductPriceLogGetRequest extends VoApiRequest<ProductPriceLogGetResponse> {

	public String getApiURLPath() {
		return "/product/priceLog/get";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * Product的Code
	 */
	private String productCode;

	/**
	 * Product的SkuCode
	 */
	private String productSkuCode;

	private int offset = 0;

	private int rows = 10;

	public ProductPriceLogGetRequest() {}

	public ProductPriceLogGetRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productCode or productSkuCode", productCode, productSkuCode);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductSkuCode() {
		return productSkuCode;
	}

	public void setProductSkuCode(String productSkuCode) {
		this.productSkuCode = productSkuCode;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}