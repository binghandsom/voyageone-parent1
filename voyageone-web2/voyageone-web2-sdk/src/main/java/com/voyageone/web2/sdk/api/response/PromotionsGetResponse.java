/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.response;

import java.util.List;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;

/**
 * /promotion/selectByCondtion Response Model
 *
 * Created on 2016-01-14
 *
 * @author binbin.gao
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PromotionsGetResponse extends VoApiListResponse {

	/** */
	private List<CmsBtPromotionModel> cmsBtPromotionModels;

	/**
	 * @return the cmsBtPromotionModels
	 */
	public List<CmsBtPromotionModel> getCmsBtPromotionModels() {
		return cmsBtPromotionModels;
	}

	/**
	 * @param cmsBtPromotionModels
	 *            the cmsBtPromotionModels to set
	 */
	public void setCmsBtPromotionModels(
			List<CmsBtPromotionModel> cmsBtPromotionModels) {
		this.cmsBtPromotionModels = cmsBtPromotionModels;
	}
}
