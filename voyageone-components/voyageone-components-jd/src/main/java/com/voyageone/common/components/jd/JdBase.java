package com.voyageone.common.components.jd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.tmall.base.TbCommon;
import com.voyageone.common.configs.beans.ShopBean;

/**
 * 京东调用基础类
 * <p/>
 * Created by jonas on 15/6/5.
 */
public abstract class JdBase {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IssueLog issueLog;

    /**
     * 获取京东API连接
     *
     * @param shopBean 店铺信息
     * @return JdClient 客户端对象
     */
    protected JdClient getDefaultClient(ShopBean shopBean) {
        return new DefaultJdClient(shopBean.getApp_url(), shopBean.getSessionKey(), shopBean.getAppKey(), shopBean.getAppSecret());
    }
    
    /**
     * 获取京东API连接
     *
     * @param shopBean 店铺信息
     * @param connectTimeout 连接超时
     * @param readTimeout 读超时
     * 
     * @return JdClient 客户端对象
     */
    protected JdClient getDefaultClient(ShopBean shopBean, int connectTimeout, int readTimeout) {
        return new DefaultJdClient(shopBean.getApp_url(), shopBean.getSessionKey(), shopBean.getAppKey(), shopBean.getAppSecret(), connectTimeout, readTimeout);
    }

    /**
     * 请求京东 API。
     *
     * @param shopBean 店铺配置
     * @param request  API 请求
     * @param <T>      API 响应
     * @return T extends AbstractResponse
     */
    protected <T extends AbstractResponse> T reqApi(ShopBean shopBean, JdRequest<T> request) throws JdException {

        return reqApi(shopBean, request, TbCommon.C_MAX_API_ERROR);
    }

    /**
     * 请求京东 API。
     *
     * @param shopBean    店铺配置
     * @param request     API 请求
     * @param maxTryCount 重试的最大次数
     * @param <T>         API 响应
     * @return T extends AbstractResponse
     */
    protected <T extends AbstractResponse> T reqApi(ShopBean shopBean, JdRequest<T> request, int maxTryCount) throws JdException {

        // 按给定的次数，进行多次的尝试
        for (int intApiErrorCount = 0; intApiErrorCount < maxTryCount; intApiErrorCount++) {

        	try {
	            JdClient client = getDefaultClient(shopBean, TbCommon.C_CONNECT_TIMEOUT, TbCommon.C_READ_TIMEOUT);
	
	            T response = client.execute(request);

	            if (response != null) {
	                return response;
	            }
	            
        	} catch (Exception ex) {
        		logger.error(ex.getMessage(), ex);
        	}
        }

        return null;
    }
}
