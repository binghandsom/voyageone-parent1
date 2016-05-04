package com.voyageone.components.intltarget.bean.guest;

/**
 * @author aooer 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetGuestV3AuthResponse {

    private String personalizationID;

    private String userId;

    private String accessToken; //PK,入口token,30分钟刷新一次（即30分钟无使用，则刷新）

    private String refreshToken;

    private String expiryTime;

    public String getPersonalizationID() {
        return personalizationID;
    }

    public void setPersonalizationID(String personalizationID) {
        this.personalizationID = personalizationID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
}
