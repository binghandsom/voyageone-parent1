package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtTagModel;

import java.util.List;


/**
 * /tag/add tag add response
 * Created on 2015-12-14
 *
 * @author jerry
 * @version 2.0.0
 * @since. 2.0.0
 */
public class TagsGetByChannelIdResponse extends VoApiResponse {

    /**
     * 数据体信息
     */
    private List<CmsBtTagModel> tags;

	public List<CmsBtTagModel> getTags() {
		return tags;
	}

	public void setTags(List<CmsBtTagModel> tags) {
		this.tags = tags;
	}
}
