package com.voyageone.web2.sdk.api;

import com.voyageone.common.util.MD5;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * 基于REST的TOP客户端。
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.0
 * @since. 2.0.0
 */
public class VoApiDefaultClient implements VoApiClient {

	private static final String APP_KEY = "app_key";
	private static final String FORMAT = "format";
	private static final String METHOD = "method";
	private static final String TIMESTAMP = "timestamp";
	private static final String VERSION = "v";
	private static final String SIGN = "sign";
	private static final String SIGN_METHOD = "sign_method";
	private static final String PARTNER_ID = "partner_id";
	private static final String SESSION = "session";

	private String serverUrl;
	private String appKey;
	private String appSecret;
	private String format = VoApiConstants.FORMAT_JSON;
	//private String signMethod = VoApiConstants.SIGN_METHOD_HMAC;
	private String signMethod = VoApiConstants.SIGN_METHOD_MD5;

	private int connectTimeout = 3000;//3秒
	private int readTimeout = 6000;//60秒
	private boolean needCheckRequest = true; // 是否在客户端校验请求
	private boolean needEnableParser = true; // 是否对响应结果进行解释
	private boolean useGzipEncoding = false; // 是否启用响应GZIP压缩


	protected RestTemplate restTemplate;

	public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl) {
		this.restTemplate = restTemplate;
		this.serverUrl = serverUrl;
		setRestTemplate();
	}

	public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl, String appKey, String appSecret) {
		this.restTemplate = restTemplate;
		this.serverUrl = serverUrl;
		this.appKey = appKey;
		this.appSecret = appSecret;
		setRestTemplate();
	}

	public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl, int connectTimeout, int readTimeout) {
		this.restTemplate = restTemplate;
		this.serverUrl = serverUrl;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		setRestTemplate();
	}

	public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl, String appKey, String appSecret, int connectTimeout, int readTimeout) {
		this.restTemplate = restTemplate;
		this.serverUrl = serverUrl;
		this.serverUrl = serverUrl;
		this.appKey = appKey;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		setRestTemplate();
	}

	public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl, String appKey, String appSecret, int connectTimeout, int readTimeout, String signMethod) {
		this.restTemplate = restTemplate;
		this.serverUrl = serverUrl;
		this.serverUrl = serverUrl;
		this.appKey = appKey;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.signMethod = signMethod;
		setRestTemplate();
	}

	private void setRestTemplate() {
		ClientHttpRequestFactory rf = restTemplate.getRequestFactory();
		if (rf instanceof SimpleClientHttpRequestFactory) {
			SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = (SimpleClientHttpRequestFactory)rf;
			simpleClientHttpRequestFactory.setConnectTimeout(1 * connectTimeout);
			simpleClientHttpRequestFactory.setReadTimeout(1 * readTimeout);
		} else if (rf instanceof HttpComponentsClientHttpRequestFactory) {
			HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = (HttpComponentsClientHttpRequestFactory)rf;
			httpComponentsClientHttpRequestFactory.setConnectTimeout(1 * connectTimeout);
			httpComponentsClientHttpRequestFactory.setReadTimeout(1 * readTimeout);
		}
	}

	@Override
	public <T extends VoApiResponse> T execute(VoApiRequest<T> request) throws ApiException {
		return execute(request, null);
	}

	public <T extends VoApiResponse> T execute(VoApiRequest<T> request, String session) throws ApiException {
		return _execute(request, session);
	}

	private <T extends VoApiResponse> T _execute(VoApiRequest<T> request, String session) throws ApiException {
		if (this.needCheckRequest) {
			try {
				request.check();
			} catch (ApiRuleException e) {
				T localResponse = null;
				try {
					localResponse = request.getResponseClass().newInstance();
				} catch (Exception xe) {
					throw new ApiException(xe);
				}
				localResponse.setCode(e.getErrCode());
				localResponse.setMessage(e.getErrMsg());
				return localResponse;
			}
		}

		return doPost(request, session);
	}

	/**
	 * URI 取得
	 * @return URI
	 */
	private URI getURI(VoApiRequest request) {
		StringBuffer reqUrl = new StringBuffer(serverUrl);
		reqUrl.append(request.getApiURLPath());

		if(reqUrl.indexOf("?") != -1){
			reqUrl.append("&");
		} else {
			reqUrl.append("?");
		}
		reqUrl.append("timestamp="+request.getTimestamp());

		//appSecret appKey
		if (appKey != null && appSecret != null) {
			reqUrl.append("&");
			reqUrl.append("appKey="+appKey);
			reqUrl.append("&");
			String sign = MD5.getMD5("voyage_sign_" + appKey + "__" + appSecret);
			reqUrl.append("sign="+sign);
		}

		return URI.create(reqUrl.toString());
	}



	public <T extends VoApiResponse> T doPost(VoApiRequest<T> request, String session) throws ApiException {

		URI uri = getURI(request);

		RequestEntity<VoApiRequest<T>> requestEntity = new RequestEntity<>(request, request.getHeaders(), request.getHttpMethod(), uri);
		ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, request.getResponseClass());
		T responseBody = responseEntity.getBody();
		if (responseBody == null) {
			VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
			throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
		}

		String code = responseBody.getCode();
		if (code == null) {
			VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70002;
			throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
		}

		if (!"0".equals(code)) {
			VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70000;
			throw new ApiException(codeEnum.getErrorCode(), responseBody.getMessage());
		}

		return responseBody;
	}

	/**
	 * 是否在客户端校验请求参数。
	 */
	public void setNeedCheckRequest(boolean needCheckRequest) {
		this.needCheckRequest = needCheckRequest;
	}

//	/**
//	 * 是否把响应字符串解释为对象。
//	 */
//	public void setNeedEnableParser(boolean needEnableParser) {
//		this.needEnableParser = needEnableParser;
//	}

//	/**
//	 * 是否采用标准化的JSON格式返回。
//	 */
//	public void setUseSimplifyJson(boolean useSimplifyJson) {
//		this.useSimplifyJson = useSimplifyJson;
//	}
//
//	/**
//	 * 是否记录API请求错误日志。
//	 */
//	public void setNeedEnableLogger(boolean needEnableLogger) {
//		VoApiLogger.setNeedEnableLogger(needEnableLogger);
//	}
//
//	/**
//	 * 是否忽略HTTPS证书校验。
//	 */
//	public void setIgnoreSSLCheck(boolean ignore) {
//		WebUtils.setIgnoreSSLCheck(ignore);
//	}

//	/**
//	 * 是否启用响应GZIP压缩
//	 */
//	public void setUseGzipEncoding(boolean useGzipEncoding) {
//		this.useGzipEncoding = useGzipEncoding;
//	}
}
