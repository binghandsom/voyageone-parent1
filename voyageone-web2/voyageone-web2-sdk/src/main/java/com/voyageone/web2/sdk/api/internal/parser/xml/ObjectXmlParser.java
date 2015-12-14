package com.voyageone.web2.sdk.api.internal.parser.xml;

import com.voyageone.web2.sdk.api.VoApiParser;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.internal.mapping.Converter;

/**
 * 单个JSON对象解释器。
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ObjectXmlParser<T extends VoApiResponse> implements VoApiParser<T> {

	private Class<T> clazz;

	public ObjectXmlParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T parse(String rsp) throws ApiException {
		Converter converter = (Converter) new XmlConverter();
		return converter.toResponse(rsp, clazz);
	}

	public Class<T> getResponseClass() {
		return clazz;
	}

}
