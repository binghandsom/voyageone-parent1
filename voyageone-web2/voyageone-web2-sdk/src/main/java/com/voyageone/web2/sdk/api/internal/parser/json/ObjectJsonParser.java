package com.voyageone.web2.sdk.api.internal.parser.json;

import com.voyageone.web2.sdk.api.VoApiParser;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.internal.mapping.Converter;

/**
 * 单个JSON对象解释器
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectJsonParser<T extends VoApiResponse> implements VoApiParser<T> {

	private Class<T> clazz;
	private boolean simplify;

	public ObjectJsonParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	public ObjectJsonParser(Class<T> clazz, boolean simplify) {
		this.clazz = clazz;
		this.simplify = simplify;
	}

	public T parse(String rsp) throws ApiException {
		Converter converter;
		if (this.simplify) {
			converter = new SimplifyJsonConverter();
		} else {
			converter = new JsonConverter();
		}
		return converter.toResponse(rsp, clazz);
	}

	public Class<T> getResponseClass() {
		return clazz;
	}

}
