package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PlatformSpecialFieldsGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /platformspecialfield/get
 *
 *
 * Created on 2015-1-15
 *
 * @author aooer
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PlatformSpecialFieldsGetRequest extends VoApiRequest<PlatformSpecialFieldsGetResponse> {

	private Integer cartId;

	private String catId;

	private String fieldId;

	private String type;

	@Override
	public String getApiURLPath() {
		return "/platformspecialfield/get";
	}


	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" cartId", cartId);
		RequestUtils.checkNotEmpty(" catId, fieldId or type", catId, fieldId, type);
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
