package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.TagsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /tag/selectListByParentTagId tag get by parent_tag_id Request
 *
 * 1.parentTagId get Tag Child
 * 2.channelId get ALl channel root tag
 *
 * Created on 2015-12-31
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagsGetRequest extends VoApiRequest<TagsGetResponse> {

	public String getApiURLPath() {
		return "/tag/selectList";
	}

	/**
	 * parentTagId
	 */
	private Integer parentTagId;

	/**
	 * channelId
	 */
	private String channelId;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId or parentTagId", channelId, parentTagId);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(Integer parentTagId) {
		this.parentTagId = parentTagId;
	}
}