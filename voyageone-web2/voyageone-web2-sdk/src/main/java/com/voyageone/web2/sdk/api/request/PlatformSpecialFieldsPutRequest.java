package com.voyageone.web2.sdk.api.request;


import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PlatformSpecialFieldsPutResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * /platformspecialfield/put Request Model
 *
 * insert active
 *
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PlatformSpecialFieldsPutRequest extends VoApiRequest<PlatformSpecialFieldsPutResponse> {

	public String getApiURLPath() {
		return "/platformspecialfield/put";
	}

	private List<CmsMtPlatformSpecialFieldModel> specialFields = new ArrayList<>();

	public PlatformSpecialFieldsPutRequest() {
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" specialFields", specialFields);
		RequestUtils.checkMinValue((long) specialFields.size(), 1, "specialFields");
		RequestUtils.checkMaxValue((long) specialFields.size(), 100, "specialFields");
	}

	public List<CmsMtPlatformSpecialFieldModel> getSpecialFields() {
		return specialFields;
	}

	public void setSpecialFields(List<CmsMtPlatformSpecialFieldModel> specialFields) {
		this.specialFields = specialFields;
	}

	public void addSpecialField(CmsMtPlatformSpecialFieldModel field) {
		specialFields.add(field);
	}
}