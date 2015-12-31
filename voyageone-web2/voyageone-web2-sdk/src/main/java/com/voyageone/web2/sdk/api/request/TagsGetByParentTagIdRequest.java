package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.TagRemoveResponse;
import com.voyageone.web2.sdk.api.response.TagsGetByParentTagIdResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /tag/selectListByParentTagId tag get by parent_tag_id Request
 *
 * 根据传入的参数取得对应Tag
 *
 * Created on 2015-12-31
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagsGetByParentTagIdRequest extends VoApiRequest<TagsGetByParentTagIdResponse> {

	public String getApiURLPath() {
		return "/tag/selectListByParentTagId";
	}

	private String channelId;

	/**
	 * parentTagId
	 */
	private Integer parentTagId;

	public TagsGetByParentTagIdRequest() {

	}

	public TagsGetByParentTagIdRequest(String channelId) {
		this.channelId = channelId;
	}

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty(channelId);
		RequestUtils.checkNotEmpty("parentTagId", parentTagId);
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