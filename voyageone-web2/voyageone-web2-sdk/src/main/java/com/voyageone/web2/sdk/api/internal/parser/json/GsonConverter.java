package com.voyageone.web2.sdk.api.internal.parser.json;

import com.voyageone.common.util.JsonUtil;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.internal.mapping.Converter;


/**
 * JSON格式转换器。
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class GsonConverter implements Converter {

	public <T extends VoApiResponse> T toResponse(String rsp, Class<T> clazz) throws ApiException {
		return JsonUtil.jsonToBean(rsp, clazz);
	}

}
