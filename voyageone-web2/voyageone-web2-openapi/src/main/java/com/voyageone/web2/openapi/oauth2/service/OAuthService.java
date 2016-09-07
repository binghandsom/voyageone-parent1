package com.voyageone.web2.openapi.oauth2.service;

import com.voyageone.service.daoext.com.ComOpenApiClientDaoExt;
import com.voyageone.service.model.openapi.oauth.ComOpenApiClientModel;
import com.voyageone.web2.openapi.oauth2.cache.OAuth2CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OAuthService
 *
 * @author chuanyu.liang
 * @version 2.0.0, 16/8/14
 */


@Service
public class OAuthService {

    private ThreadLocal<ComOpenApiClientModel> currentThreadClientModel = new ThreadLocal<>();

    private static final String CACHE_BIZ_NAME = "AccessToken";

    @Autowired
    private ComOpenApiClientDaoExt clientDao;

    @Autowired
    private OAuth2CacheService cacheService;

    //添加 access token
    public void addAccessToken(String accessToken, String clientId) {
        cacheService.setCache(CACHE_BIZ_NAME, accessToken, clientId);
    }

    // 取得ClientId
    private String getClientIdByAccessToken(String accessToken) {
        return (String) cacheService.getCache(CACHE_BIZ_NAME, accessToken);
    }

    // 删除
    private void removeByAccessToken(String accessToken) {
        cacheService.deleteCache(CACHE_BIZ_NAME, accessToken);
    }

    //验证access token是否有效
    public boolean checkAccessToken(String accessToken) {
        boolean isHaveToken = getClientIdByAccessToken(accessToken) != null;
        if (!isHaveToken) {
            removeByAccessToken(accessToken);
        }
        return isHaveToken;
    }

    // 检查客户端客户端id, 安全KEY是否正确
    public boolean checkClientSecret(String clientId, String clientSecret) {
        return findByClientSecret(clientId, clientSecret) != null;
    }

    // 检查客户端客户端id, RefreshToken是否正确
    public boolean checkRefreshToken(String clientId, String refreshToken) {
        return findByClientSecret(clientId, refreshToken) != null;
    }

    //auth code / access token 过期时间
    public long getExpireIn() {
        return 3600L;
    }

    private ComOpenApiClientModel findByClientSecret(String clientId, String clientSecret) {
        return clientDao.selectByClientSecret(clientId, clientSecret);
    }

    public ComOpenApiClientModel getClientSecretAndSetCurrentThread(String clientId, String clientSecret) {
        ComOpenApiClientModel model = clientDao.selectByClientSecret(clientId, clientSecret);
        if (model != null) {
            currentThreadClientModel.set(model);
        }
        return model;
    }

    public ComOpenApiClientModel getCurrentThreadClientModel() {
        return currentThreadClientModel.get();
    }
}
