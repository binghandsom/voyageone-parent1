package com.voyageone.web2.sdk.api.util;

import com.voyageone.web2.sdk.api.internal.util.VoApiHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * API请求参数容器
 * 
 * @author fengsheng
 * @since Sep 19, 2014
 */
public class RequestParametersHolder {

	private String requestUrl;
	private String responseBody;

	private VoApiHashMap protocalMustParams;
	private VoApiHashMap protocalOptParams;
	private VoApiHashMap applicationParams;

	public String getRequestUrl() {
		return this.requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getResponseBody() {
		return this.responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public VoApiHashMap getProtocalMustParams() {
		return this.protocalMustParams;
	}

	public void setProtocalMustParams(VoApiHashMap protocalMustParams) {
		this.protocalMustParams = protocalMustParams;
	}

	public VoApiHashMap getProtocalOptParams() {
		return this.protocalOptParams;
	}

	public void setProtocalOptParams(VoApiHashMap protocalOptParams) {
		this.protocalOptParams = protocalOptParams;
	}

	public VoApiHashMap getApplicationParams() {
		return this.applicationParams;
	}

	public void setApplicationParams(VoApiHashMap applicationParams) {
		this.applicationParams = applicationParams;
	}

	public Map<String, String> getAllParams() {
		Map<String, String> params = new HashMap<String, String>();
		if (protocalMustParams != null && !protocalMustParams.isEmpty()) {
			params.putAll(protocalMustParams);
		}
		if (protocalOptParams != null && !protocalOptParams.isEmpty()) {
			params.putAll(protocalOptParams);
		}
		if (applicationParams != null && !applicationParams.isEmpty()) {
			params.putAll(applicationParams);
		}
		return params;
	}

}
