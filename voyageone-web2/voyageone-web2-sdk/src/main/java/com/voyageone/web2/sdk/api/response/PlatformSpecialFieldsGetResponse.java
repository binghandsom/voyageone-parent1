package com.voyageone.web2.sdk.api.response;

import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;


/**
 * /platformspecialfield/get response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PlatformSpecialFieldsGetResponse extends VoApiListResponse {

	/**
	 * 数据体信息
	 */
    private List<CmsMtPlatformSpecialFieldModel> fields;

	public List<CmsMtPlatformSpecialFieldModel> getFields() {
		return fields;
	}

	public void setFields(List<CmsMtPlatformSpecialFieldModel> fields) {
		this.fields = fields;
	}
}
