package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
public class ProductGroupsGetRequest extends VoApiListRequest<ProductGroupsGetResponse> {

	public String getApiURLPath() {
		return "/puroduct/group/selectList";
	}

	private String channelId;

	/**
	 * groupId
	 */
	private Set<Long> groupIds = new LinkedHashSet<>();

	/**
	 * cartId
	 */
	private Integer cartId;

	/**
	 * numIId
	 */
	private Set<String> numIIds = new LinkedHashSet<>();

	/**
	 * props
	 * 同 product
	 */
	private String props;

	public ProductGroupsGetRequest() {

	}

	public ProductGroupsGetRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty(" groupIds, cartId, numIId or props", groupIds, cartId, numIIds, props);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Set<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(Set<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Set<String> getNumIIds() {
		return numIIds;
	}

	public void setNumIIds(Set<String> numIIds) {
		this.numIIds = numIIds;
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