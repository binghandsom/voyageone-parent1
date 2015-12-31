package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import com.voyageone.web2.sdk.api.response.TagRemoveResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /tag/remove tag remove Request
 *
 * 根据传入的参数删除对应Tag
 *
 * Created on 2015-12-31
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagRemoveRequest extends VoApiRequest<TagRemoveResponse> {

	public String getApiURLPath() {
		return "/tag/remove";
	}

	private String channelId;

	/**
	 * tagId
	 */
	private Integer tagId;

	/**
	 * modifier
	 */
	private String modifier;

	public TagRemoveRequest() {

	}

	public TagRemoveRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty("tagId", tagId);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
}