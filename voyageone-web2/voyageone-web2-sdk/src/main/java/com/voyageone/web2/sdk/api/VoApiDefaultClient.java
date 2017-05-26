package com.voyageone.web2.sdk.api;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.AccessTokenRequest;
import com.voyageone.web2.sdk.api.response.AccessTokenResponse;
import org.springframework.http.*;
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
 * @since 2.0.0
 */
public class VoApiDefaultClient implements VoApiClient {

    private static final String TIMESTAMP = "timestamp";

    private static final String ACCESS_TOKEN = "oauth_token";

    private String serverUrl;

    private static String accessTokenUri = "/accessToken";

    private String clientId;
    private String clientSecret;
    private String grantType = "password";

    private String accessToken;


    private int connectTimeout = 3000;//3秒
    private int readTimeout = 6000;//60秒

    private boolean needCheckAccessToken = true; // 是否校验AccessToken
    private boolean needCheckRequest = true; // 是否在客户端校验请求
    private boolean needEnableParser = true; // 是否对响应结果进行解释
    private boolean useGzipEncoding = false; // 是否启用响应GZIP压缩

    private RestTemplate restTemplate;

    public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
        setRestTemplate();
    }

    public VoApiDefaultClient(RestTemplate restTemplate, String serverUrl, int connectTimeout, int readTimeout) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        setRestTemplate();
    }

    private void setRestTemplate() {
        ClientHttpRequestFactory rf = restTemplate.getRequestFactory();
        if (rf instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = (SimpleClientHttpRequestFactory) rf;
            simpleClientHttpRequestFactory.setConnectTimeout(connectTimeout);
            simpleClientHttpRequestFactory.setReadTimeout(readTimeout);
        } else if (rf instanceof HttpComponentsClientHttpRequestFactory) {
            HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = (HttpComponentsClientHttpRequestFactory) rf;
            httpComponentsClientHttpRequestFactory.setConnectTimeout(connectTimeout);
            httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout);
        }
    }

    @Override
    public <T extends VoApiResponse> T execute(VoApiRequest<T> request) throws ApiException {
        T result;
        try {
            result = doPost(request);
        } catch (ApiException ae) {
            // invalid token.
            if (VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70099.getErrorCode().equals(ae.getErrCode())) {
                accessToken = getRemoteAccessToken();
                result = doPost(request);
            } else {
                throw ae;
            }
        }
        return result;
    }

    /**
     * URI 取得
     */
    private URI getURI(VoApiRequest request) {
        StringBuilder reqUrl = new StringBuilder(serverUrl);
        reqUrl.append(request.getApiURLPath());

        if (reqUrl.indexOf("?") != -1) {
            reqUrl.append("&");
        } else {
            reqUrl.append("?");
        }
        reqUrl.append(TIMESTAMP + "=").append(request.getTimestamp());

        // CheckAccessToken
        if (needCheckAccessToken) {
            if (accessToken == null) {
                // get accessToken
                accessToken = getRemoteAccessToken();
                // check accessToken
                if (accessToken == null || "".equals(accessToken)) {
                    VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70091;
                    throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
                }
            }
        }

        //ACCESS_TOKEN
        if (accessToken != null) {
            reqUrl.append("&");
            reqUrl.append(ACCESS_TOKEN).append("=").append(accessToken);
        }

        return URI.create(reqUrl.toString());
    }

    private <T extends VoApiResponse> T doPost(VoApiRequest<T> request) throws ApiException {
        URI uri = getURI(request);
        RequestEntity<VoApiRequest<T>> requestEntity = new RequestEntity<>(request, request.getHeaders(), request.getHttpMethod(), uri);
        return doPost(requestEntity, request.getResponseClass());
    }

    private <T extends VoApiResponse> T doPost(RequestEntity<VoApiRequest<T>> requestEntity, Class<T> responseClass) throws ApiException {
        ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, responseClass);
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
            throw new ApiException(code, responseBody.getMessage());
        }

        return responseBody;
    }

    @SuppressWarnings("unchecked")
    private String getRemoteAccessToken() {
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest(accessTokenUri, clientId, clientSecret, grantType);
        // Create Url
        String reqUrl = serverUrl + accessTokenRequest.getApiURLPath() + "&" + TIMESTAMP + "=" + DateTimeUtil.getNowTimeStampLong();
        URI uri = URI.create(reqUrl);
        // send url
        RequestEntity requestEntity = new RequestEntity<>(HttpMethod.POST, uri);
        AccessTokenResponse accessTokenResponse = doPost(requestEntity, accessTokenRequest.getResponseClass());
        // get url
        return accessTokenResponse.getAccess_token();
    }

    public boolean isNeedCheckAccessToken() {
        return needCheckAccessToken;
    }

    public void setNeedCheckAccessToken(boolean needCheckAccessToken) {
        this.needCheckAccessToken = needCheckAccessToken;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isNeedCheckRequest() {
        return needCheckRequest;
    }

    /**
     * 是否在客户端校验请求参数。
     */
    public void setNeedCheckRequest(boolean needCheckRequest) {
        this.needCheckRequest = needCheckRequest;
    }

    public boolean isNeedEnableParser() {
        return needEnableParser;
    }

    public void setNeedEnableParser(boolean needEnableParser) {
        this.needEnableParser = needEnableParser;
    }

    public boolean isUseGzipEncoding() {
        return useGzipEncoding;
    }

    public void setUseGzipEncoding(boolean useGzipEncoding) {
        this.useGzipEncoding = useGzipEncoding;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
