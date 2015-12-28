package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * get main group Request Model
 *
 * /puroduct/group/selectOne
 *
 *  1.传入groupId来查询
 *  2.传入cartId&numIId来查询
 *  3:传入props来查询
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupGetRequest extends VoApiRequest<ProductGroupGetResponse> {

	public String getApiURLPath() {
		return "/puroduct/group/selectOne";
	}

	private String channelId;

	/**
	 * groupId
	 */
	private Long groupId;

	/**
	 * cartId
	 */
	private Integer cartId;

	/**
	 * numIId
	 */
	private String numIId;

	/**
	 * props
	 * 同 product
	 */
	private String props;

	public ProductGroupGetRequest() {

	}

	public ProductGroupGetRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" groupId, cartId, numIId or props", groupId, cartId, numIId, props);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public String getNumIId() {
		return numIId;
	}

	public void setNumIId(String numIId) {
		this.numIId = numIId;
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