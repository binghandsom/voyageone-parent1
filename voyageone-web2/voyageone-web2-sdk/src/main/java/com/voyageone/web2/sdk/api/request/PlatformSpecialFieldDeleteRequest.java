package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.PlatformSpecialFieldDeleteResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * /platformspecialfield/delete Request Model
 *
 *
 * Created on 2015-1-15
 *
 * @author aooer
 * @version 2.0.0
 * @since. 2.0.0
 */
public class PlatformSpecialFieldDeleteRequest extends VoApiRequest<PlatformSpecialFieldDeleteResponse> {

	private Integer cartId;

	private String catId;

	private String fieldId;

	private String type;

	@Override
	public String getApiURLPath() {
		return "/platformspecialfield/delete";
	}


	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" cartId", cartId);
		RequestUtils.checkNotEmpty(" catId", catId);
		RequestUtils.checkNotEmpty(" fieldId", fieldId);
		RequestUtils.checkNotEmpty(" type", type);
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
