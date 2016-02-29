package com.voyageone.web2.sdk.api.request;


import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.FeedMappingsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /feedMapping/get Request Model
 *
 * feedMapping
 *  1.传入channel, feedCategoryPath, mainCategoryPath 来查询
 *  2.返回FeedMapping list
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class FeedMappingsGetRequest extends VoApiRequest<FeedMappingsGetResponse> {

	public String getApiURLPath() {
		return "/feedMapping/get";
	}

	/**
	 * channelId
	 */
	private String channelId;

	/**
	 * feedCategoryPath
	 */
	private String feedCategoryPath;

	/**
	 * mainCategoryPath
	 */
	private String mainCategoryPath;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
		if (feedCategoryPath != null || mainCategoryPath != null) {
			RequestUtils.checkNotEmpty(" feedCategoryPath", feedCategoryPath);
			RequestUtils.checkNotEmpty(" mainCategoryPath", mainCategoryPath);
		}
	}

	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getFeedCategoryPath() {
		return feedCategoryPath;
	}
	public void setFeedCategoryPath(String feedCategoryPath) {
		this.feedCategoryPath = feedCategoryPath;
	}

	public String getMainCategoryPath() {
		return mainCategoryPath;
	}
	public void setMainCategoryPath(String mainCategoryPath) {
		this.mainCategoryPath = mainCategoryPath;
	}
}