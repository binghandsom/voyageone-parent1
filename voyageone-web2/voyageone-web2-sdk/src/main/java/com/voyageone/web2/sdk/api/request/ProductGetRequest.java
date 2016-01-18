package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /product/selectOne Request Model
 *
 * Product的id.两种方式来查看一个产品:
 *  1.传入productId来查询
 *  2.传入productCode来查询
 *  3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGetRequest extends VoApiRequest<ProductGetResponse> {

	public String getApiURLPath() {
		return "/product/selectOne";
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
	 * query String
	 */
	private String queryString;

	/**
	 * 关键属性,结构：pid1:value1;pid2:value2，如果有型号，系列等子属性用: 隔开 例如：“20000:优衣库:型号:001;632501:1234”，表示“品牌:优衣库:型号:001;货号:1234”
	 *
	 * 例如 获取model所属所有商品
	 *
	 */
	private String props;

	public ProductGetRequest() {

	}

	public ProductGetRequest(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" productId productCode queryString props", productId, productCode, queryString, props);
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

	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getProps() {
		return props;
	}
	public void setProps(String props) {
		this.props = props;
	}

	public void addProp(String key, Object value) {
		this.props = RequestUtils.addProp(props, key, value);
	}


}