package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.TagAddResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /tag/add tag add Request
 *
 * 根据传入的参数新生成Tag
 *
 * Created on 2015-12-29
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagAddRequest extends VoApiRequest<TagAddResponse> {

	public String getApiURLPath() {
		return "/tag/add";
	}

	private String channelId;

	/**
	 * tagName
	 */
	private String tagName;

	/**
	 * tagType
	 */
	private Integer tagType;

	/**
	 * tagStatus
	 */
	private Integer tagStatus;

	/**
	 * sortOrder
	 */
	private Integer sortOrder;

	/**
	 * parentTagId
	 */
	private Integer parentTagId;

	/**
	 * creater
	 */
	private String creater;

	public void check() throws ApiRuleException {
		super.check();
		RequestUtils.checkNotEmpty("channelId", channelId);
		RequestUtils.checkNotEmpty("tagName", tagName);
		RequestUtils.checkNotEmpty("tagType", tagType);
		RequestUtils.checkNotEmpty("tagStatus",tagStatus);
		RequestUtils.checkNotEmpty("sortOrder",sortOrder);
		RequestUtils.checkNotEmpty("parentTagId",parentTagId);
		RequestUtils.checkNotEmpty("creater",creater);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Integer getTagType() {
		return tagType;
	}

	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}

	public Integer getTagStatus() {
		return tagStatus;
	}

	public void setTagStatus(Integer tagStatus) {
		this.tagStatus = tagStatus;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(Integer parentTagId) {
		this.parentTagId = parentTagId;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}
}