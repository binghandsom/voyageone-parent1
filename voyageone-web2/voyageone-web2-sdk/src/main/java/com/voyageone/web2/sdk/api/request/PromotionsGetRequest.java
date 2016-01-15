/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;

/**
 * @description
 *
 * @author gbb
 */
public class PromotionsGetRequest extends VoApiRequest<PromotionsGetResponse> {

	/** channelId */
	private String channelId;

	/** promotionId */
	private Integer promotionId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voyageone.web2.sdk.api.VoApiRequest#getApiURLPath()
	 */
	@Override
	public String getApiURLPath() {
		return "/promotion/selectByCondtion";
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId
	 *            the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the promotionId
	 */
	public Integer getPromotionId() {
		return promotionId;
	}

	/**
	 * @param promotionId
	 *            the promotionId to set
	 */
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

}
