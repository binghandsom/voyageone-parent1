package com.voyageone.web2.sdk.api;

import com.voyageone.web2.sdk.api.exception.ApiException;

/**
 * VoApi客户端。
 *
 * @author chuanyu.liang 2015/12/10
 * @version 2.0.1
 * @since. 2.0.0
 */
public interface VoApiClient {

    /**
     * 执行TOP公开API请求。
     * @param <T>
     * @param request 具体的TOP请求
     * @return
     * @throws ApiException
     */
    public <T extends VoApiResponse> T execute(VoApiRequest<T> request) throws ApiException;
    /**
     * 执行TOP隐私API请求。
     * @param <T>
     * @param request 具体的TOP请求
     * @param session 用户会话授权码
     * @return
     * @throws ApiException
     */
    public <T extends VoApiResponse> T execute(VoApiRequest<T> request, String session) throws ApiException ;
}
