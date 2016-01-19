/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionsGetResponse;

/**
 * /promotion/selectByCondtion
 *
 * 1 selectById
 * 2 selectByOtherCondtion
 *
 * Created on 2015-1-15
 *
 * @author aooer
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PromotionsGetRequest extends VoApiListRequest<PromotionsGetResponse> {

	/** channelId */
	private String channelId;

	/** promotionId */
	private Integer promotionId;

	private String promotionName;

	private Integer cartId;

	private String promotionType;

	private String createdStart;

	private String createdEnd;

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

	@Override
	public void requestCheck() throws ApiRuleException {

	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(String createdStart) {
		this.createdStart = createdStart;
	}

	public String getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = createdEnd;
	}
}
