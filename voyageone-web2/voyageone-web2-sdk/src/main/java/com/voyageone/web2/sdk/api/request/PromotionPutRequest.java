package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PromotionsPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /promotion/saveOrUpdate Request Model
 *
 * 1. add cmsBtPromotionModel
 * 2. cmsBtPromotionModel.
 *
 * Created on 2016-01-14
 *
 * @author binbin.gao
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PromotionPutRequest extends VoApiRequest<PromotionsPutResponse> {

	/** model */
	private CmsBtPromotionModel cmsBtPromotionModel;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.voyageone.web2.sdk.api.VoApiRequest#getApiURLPath()
	 */
	@Override
	public String getApiURLPath() {
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
	 * @param cmsBtPromotionModel model
	 */
	public PromotionPutRequest(CmsBtPromotionModel cmsBtPromotionModel) {
		super();
		this.cmsBtPromotionModel = cmsBtPromotionModel;
	}

	/**
	 * 
	 */
	public PromotionPutRequest() {
		super();
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" cmsBtPromotionModel", cmsBtPromotionModel);
		if (cmsBtPromotionModel.getPromotionId() >0) {
			//add
			RequestUtils.checkNotEmpty(" cmsBtPromotionModel.channelId", cmsBtPromotionModel.getChannelId());
		} else {
			//update

		}
	}

}
