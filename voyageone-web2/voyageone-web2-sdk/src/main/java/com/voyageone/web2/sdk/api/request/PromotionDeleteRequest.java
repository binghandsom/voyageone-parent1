/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;
import jdk.nashorn.internal.ir.RuntimeNode;

/**
 * /promotion/deleteById
 *
 * 根据id删除promotion
 *
 * Created on 2015-1-15
 *
 * @author aooer
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PromotionDeleteRequest extends
		VoApiRequest<PromotionsPutResponse> {

	/** promotionId */
	private Integer promotionId;

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

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("promotionId",promotionId);
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
