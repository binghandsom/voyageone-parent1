package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /group/selectList Request Model
 *
 * channelId groupId查找 groupsData:
 *
 * Created on 2015-1-28
 *
 * @author lewis
 * @version 2.0.0
 * @since. 2.0.0
 */
public class GroupMainProductUpdateRequest extends VoApiRequest<VoApiUpdateResponse> {

	public String getApiURLPath() {
		return "/product/group/updMainProduct";
	}


	public GroupMainProductUpdateRequest() {

	}

	public GroupMainProductUpdateRequest(String channelId, Long groupId, Long productId) {
		this.channelId = channelId;
		this.groupId = groupId;
		this.productId = productId;
	}

	private String channelId;

	private Long groupId;

	private Long productId;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId groupId productId", channelId,groupId,productId);
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}