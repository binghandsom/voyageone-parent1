package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGroupsDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * remove group Request Model
 *
 * /product/group/delete
 *
 *  1.传入groupId来删除
 *  2.传入cartId&numIIds来删除
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductGroupsDeleteRequest extends VoApiListRequest<ProductGroupsDeleteResponse> {

	public String getApiURLPath() {
		return "/product/group/delete";
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


	public ProductGroupsDeleteRequest() {

	}

	public ProductGroupsDeleteRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(" channelId", channelId);
		RequestUtils.checkNotEmpty(" groupIds, numIIds", groupIds, numIIds);
		if (groupIds != null && groupIds.size()>0) {
			RequestUtils.checkMinValue((long) groupIds.size(), 1, "groupIds");
			RequestUtils.checkMaxValue((long) groupIds.size(), 100, "groupIds");
		}
		if (numIIds != null && numIIds.size()>0) {
			RequestUtils.checkMinValue((long) numIIds.size(), 1, "numIIds");
			RequestUtils.checkMaxValue((long) numIIds.size(), 100, "numIIds");
			RequestUtils.checkNotEmpty(" cartId", cartId);
		}
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

	public void addGroupId(Long groupId) {
		groupIds.add(groupId);
	}

}