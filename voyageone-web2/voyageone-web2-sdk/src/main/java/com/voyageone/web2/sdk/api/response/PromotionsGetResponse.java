/**
 * (c) Copyright Voyageone Corp 2016
 */
package com.voyageone.web2.sdk.api.response;

import java.util.List;

import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;

/**
 * @description 查询响应数据
 * @author gbb
 */
public class PromotionsGetResponse extends VoApiUpdateResponse {

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
