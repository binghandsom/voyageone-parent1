package com.voyageone.web2.sdk.api.response;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;


/**
 * /feedMappings/get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class FeedMappingsGetResponse extends VoApiListResponse {

	/**
	 * 数据体信息
	 */
    private List<CmsBtFeedMappingModel> feedMappings;

	public List<CmsBtFeedMappingModel> getFeedMappings() {
		return feedMappings;
	}

	public void setFeedMappings(List<CmsBtFeedMappingModel> feedMappings) {
		this.feedMappings = feedMappings;
	}
}
