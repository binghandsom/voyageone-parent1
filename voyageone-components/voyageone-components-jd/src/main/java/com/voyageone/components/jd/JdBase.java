package com.voyageone.components.jd;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.ComponentConstants;
import com.voyageone.service.dao.com.ComBtJingdongApiLogDao;
import com.voyageone.service.model.com.ComBtJingdongApiLogModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 京东调用基础类
 * <p/>
 * Created by jonas on 15/6/5.
 */
public abstract class JdBase extends ComponentBase {

	@Autowired
	private ComBtJingdongApiLogDao apiLogDao;

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
    protected <T extends AbstractResponse> T reqApi(ShopBean shopBean, JdRequest<T> request) {
        return reqApi(shopBean, request, ComponentConstants.C_MAX_API_ERROR);
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
    protected <T extends AbstractResponse> T reqApi(ShopBean shopBean, JdRequest<T> request, int maxTryCount) {
        // 按给定的次数，进行多次的尝试
        for (int intApiErrorCount = 0; intApiErrorCount < maxTryCount; intApiErrorCount++) {

			try {
				apiLogDao.insert(new ComBtJingdongApiLogModel(request.getApiMethod(), shopBean));
			} catch (Exception ignored) {
			}

			try {
	            JdClient client = getDefaultClient(shopBean, ComponentConstants.C_CONNECT_TIMEOUT, ComponentConstants.C_READ_TIMEOUT);
	
	            T response = client.execute(request);

	            if (response != null) {
                    if (!StringUtils.isEmpty(response.getZhDesc())
                            && response.getZhDesc().contains("平台连接后端服务不可用")
                            && intApiErrorCount < maxTryCount - 1) {
                        continue;
                    }
	                return response;
	            }
	            
        	} catch (Exception ex) {
        		logger.error(ex.getMessage(), ex);
        	}
        }

        return null;
    }
}
