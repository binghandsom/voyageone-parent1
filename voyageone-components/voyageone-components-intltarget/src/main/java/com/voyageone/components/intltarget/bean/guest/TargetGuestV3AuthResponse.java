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
}
