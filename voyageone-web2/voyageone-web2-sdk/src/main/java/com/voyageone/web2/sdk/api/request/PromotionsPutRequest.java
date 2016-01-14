/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;

/**
 * @description 添加或修改请求
 * @author gbb
 */
public class PromotionsPutRequest extends VoApiRequest<PromotionsPutResponse> {

	/** 模型 */
	private CmsBtPromotionModel cmsBtPromotionModel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.voyageone.web2.sdk.api.VoApiRequest#getApiURLPath()
	 */
	@Override
	public String getApiURLPath() {
		// TODO Auto-generated method stub
		return "/promotion/saveOrUpdate";
	}

	/**
	 * @return the cmsBtPromotionModel
	 */
	public CmsBtPromotionModel getCmsBtPromotionModel() {
		return cmsBtPromotionModel;
	}

	/**
	 * @param cmsBtPromotionModel
	 *            the cmsBtPromotionModel to set
	 */
	public void setCmsBtPromotionModel(CmsBtPromotionModel cmsBtPromotionModel) {
		this.cmsBtPromotionModel = cmsBtPromotionModel;
	}

	/**
	 * @param cmsBtPromotionModel
	 */
	public PromotionsPutRequest(CmsBtPromotionModel cmsBtPromotionModel) {
		super();
		this.cmsBtPromotionModel = cmsBtPromotionModel;
	}

	/**
	 * 
	 */
	public PromotionsPutRequest() {
		super();
	}

}
