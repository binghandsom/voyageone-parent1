package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.TagsGetByChannelIdResponse;
import com.voyageone.web2.sdk.api.response.TagsGetByParentTagIdResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /tag/selectParentTagByChannel tag get by channelId Request
 *
 * 根据传入的参数取得对应Tag
 *
 * Created on 2015-12-31
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagsGetByChannelIdRequest extends VoApiRequest<TagsGetByChannelIdResponse> {

	public String getApiURLPath() {
		return "/tag/selectParentTagByChannel";
	}

	private String channelId;

	public TagsGetByChannelIdRequest() {

	}

	public TagsGetByChannelIdRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}