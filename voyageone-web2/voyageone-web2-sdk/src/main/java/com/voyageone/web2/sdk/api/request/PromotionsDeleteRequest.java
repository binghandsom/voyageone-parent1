/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;

/**
 * @description
 *
 * @author gbb
 */
public class PromotionsDeleteRequest extends
		VoApiRequest<PromotionsPutResponse> {

	/** channelId */
	private String channelId;

	/** promotionId */
	private Long promotionId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voyageone.web2.sdk.api.VoApiRequest#getApiURLPath()
	 */
	@Override
	public String getApiURLPath() {
		// TODO Auto-generated method stub
		return "/promotion/deleteById";
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
	public Long getPromotionId() {
		return promotionId;
	}

	/**
	 * @param promotionId
	 *            the promotionId to set
	 */
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

}
