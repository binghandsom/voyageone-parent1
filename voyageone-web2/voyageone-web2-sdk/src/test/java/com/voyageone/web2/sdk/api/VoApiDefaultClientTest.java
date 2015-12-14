package com.voyageone.web2.sdk.api;

import com.voyageone.web2.sdk.api.exception.ApiException;
import org.junit.Test;

/**
 * Created by DELL on 2015/12/10.
 */
public class VoApiDefaultClientTest {

    public <T extends VoApiResponse> T execute(VoApiRequest<T> request) throws ApiException {
        VoApiClient client = getDefaultApiClient();
        return client.execute(request);
    }

    protected VoApiClient getDefaultApiClient() {
        return getDefaultApiClient("json");
    }

    public static final int C_CONNECT_TIMEOUT = 20000;
    public static final int C_READ_TIMEOUT = 20000;
    public static final int C_MAX_API_ERROR = 3;
    public static final int TRY_WAIT_TIME_4TAOBAO = 5000;

    private static String apiUrl = "";
    private static String appKey = "";
    private static String appSecret = "";

    protected VoApiClient getDefaultApiClient(String format) {
        if (format == null || "".equals(format.trim()))
            throw new IllegalArgumentException("必须为淘宝 API 调用，指定通讯内容的格式，如“json”");

        return new VoApiDefaultClient(apiUrl, appKey, appSecret, format, C_CONNECT_TIMEOUT, C_READ_TIMEOUT);
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static void setApiUrl(String apiUrl) {
        VoApiDefaultClientTest.apiUrl = apiUrl;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static void setAppSecret(String appSecret) {
        VoApiDefaultClientTest.appSecret = appSecret;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        VoApiDefaultClientTest.appKey = appKey;
    }
}
