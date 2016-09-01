package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.AccessTokenResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 * AccessTokenRequest
 * Created on 2016-08-15
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since 2.0.0
 */
public class AccessTokenRequest extends VoApiRequest<AccessTokenResponse> {

	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String GRANT_TYPE = "grant_type";

	private String accessTokenUri;
	private String clientId;
	private String clientSecret;
	private String grantType;

	public AccessTokenRequest(String accessTokenUri, String clientId, String clientSecret, String grantType) {
		this.accessTokenUri = accessTokenUri;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = grantType;
	}

	@Override
	public String getApiURLPath() {
		requestCheck();

		return  accessTokenUri +
				"?" +
				CLIENT_ID + "=" + clientId +
				"&" +
				CLIENT_SECRET + "=" + clientSecret +
				"&" +
				GRANT_TYPE + "=" + grantType;
	}

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("accessTokenUri", accessTokenUri);
		RequestUtils.checkNotEmpty(CLIENT_ID, clientId);
		RequestUtils.checkNotEmpty(CLIENT_SECRET, clientSecret);
		RequestUtils.checkNotEmpty(GRANT_TYPE, grantType);
	}

	public String getAccessTokenUri() {
		return accessTokenUri;
	}

	public void setAccessTokenUri(String accessTokenUri) {
		this.accessTokenUri = accessTokenUri;
	}

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
}