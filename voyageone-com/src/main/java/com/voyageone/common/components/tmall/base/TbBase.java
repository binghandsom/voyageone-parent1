package com.voyageone.common.components.tmall.base;

import com.taobao.api.*;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 为 淘宝 API 提供基础服务
 * <p/>
 * Created by neil on 2015-05-22.
 */
public abstract class TbBase {

    @Autowired
    protected IssueLog issueLog;

    protected Log logger = LogFactory.getLog(getClass());

    /**
     * 获取淘宝API连接，使用默认的 json 格式
     *
     * @param shopBean 店铺信息
     * @return TaobaoClient 客户端对象
     */
    protected TaobaoClient getDefaultTaobaoClient(ShopBean shopBean) {
        return getDefaultTaobaoClient(shopBean, "json");
    }

    /**
     * 获取淘宝API连接
     *
     * @param shopBean 店铺信息
     * @param format   调用 API 时，传值的格式。
     * @return TaobaoClient 客户端对象
     */
    protected TaobaoClient getDefaultTaobaoClient(ShopBean shopBean, String format) {

        if (StringUtils.isEmpty(format))
            throw new IllegalArgumentException("必须为淘宝 API 调用，指定通讯内容的格式，如“json”");

        return new DefaultTaobaoClient(shopBean.getApp_url(), shopBean.getAppKey(), shopBean.getAppSecret(), format,
                TbCommon.C_CONNECT_TIMEOUT, TbCommon.C_READ_TIMEOUT);
    }

    /**
     * 请求淘宝 API。默认传入 Session Key
     *
     * @param shopBean 店铺配置
     * @param request  淘宝的 API 请求
     * @param <T>      淘宝的 API 响应
     * @return T extends TaobaoResponse
     */
    protected <T extends TaobaoResponse> T reqTaobaoApi(ShopBean shopBean, TaobaoRequest<T> request)
            throws ApiException {

        return reqTaobaoApi(shopBean, request, true);
    }

    /**
     * 请求淘宝 API。
     *
     * @param shopBean      店铺配置
     * @param request       淘宝的 API 请求
     * @param setSessionKey 是否传入 Session Key
     * @param <T>           淘宝的 API 响应
     * @return T extends TaobaoResponse
     */
    protected <T extends TaobaoResponse> T reqTaobaoApi(ShopBean shopBean, TaobaoRequest<T> request,
                                                        boolean setSessionKey)
            throws ApiException {

        return reqTaobaoApi(shopBean, request, TbCommon.C_MAX_API_ERROR, TbCommon.TRY_WAIT_TIME_4TAOBAO, setSessionKey);
    }

    /**
     * 请求淘宝 API。
     *
     * @param shopBean      店铺配置
     * @param request       淘宝的 API 请求
     * @param maxTryCount   重试的最大次数
     * @param tryWait       出现异常重试时的等待时间（毫秒单位）
     * @param setSessionKey 是否传入 Session Key
     * @param <T>           淘宝的 API 响应
     * @return T extends TaobaoResponse
     */
    protected <T extends TaobaoResponse> T reqTaobaoApi(ShopBean shopBean, TaobaoRequest<T> request, int maxTryCount,
                                                        int tryWait, boolean setSessionKey)
            throws ApiException {

        // 按给定的次数，进行多次的尝试
        for (int intApiErrorCount = 0; intApiErrorCount < maxTryCount; intApiErrorCount++) {

            try {
                T response = reqTaobaoApiWithThread(shopBean, request, setSessionKey, tryWait);

                if (response != null && !isTimeout(response))
                    return response;

            } catch (ApiException e) {

                // 最后一次出错则直接抛出
                if (maxTryCount - intApiErrorCount == 1) throw e;

                // 否则等待一段时间
                try {
                    Thread.sleep(tryWait);
                } catch (InterruptedException ignore) {
                }

            }
        }

        return null;
    }

    /**
     * 请求taobao Api的线程，响应和异常通过成员response和apiException传递
     * 如果超时，response为null
     * @param <T>
     */
    private class ReqTaobaoApiThread<T extends TaobaoResponse> extends Thread {
        private ShopBean shopBean;
        private TaobaoRequest<T> request;
        private boolean setSessionKey;
        private ApiException apiException = null;
        private T response = null;

        public ReqTaobaoApiThread(ShopBean shopBean, TaobaoRequest<T> request, boolean setSessionKey) {
            this.shopBean = shopBean;
            this.request = request;
            this.setSessionKey = setSessionKey;
        }

        @Override
        public void run() {
            TaobaoClient client = getDefaultTaobaoClient(shopBean);
                try {
                    response = setSessionKey
                            ? client.execute(request, shopBean.getSessionKey())
                            : client.execute(request);
                }
                catch (ApiException e) {
                    apiException = e;
                }
        }

        public ApiException getApiException() {
            return apiException;
        }

        public T getResponse() {
            return response;
        }
    }
    private <T extends TaobaoResponse> T reqTaobaoApiWithThread(ShopBean shopBean, TaobaoRequest<T> request, boolean setSessionKey, int timeout) throws ApiException {

        T response;
        ReqTaobaoApiThread t = new ReqTaobaoApiThread<T>(shopBean, request, setSessionKey);
        t.start();
        try {
            t.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (t.getApiException() != null)
        {
            throw t.getApiException();
        } else {
            response = (T) t.getResponse();
        }
        return response;
    }
    /**
     * 辅助：判断结果是否是超时
     */
    private <T extends TaobaoResponse> boolean isTimeout(T response) {
        return "isp.top-remote-connection-timeout".equals(response.getSubCode());
    }

    protected void setErrorLog(Exception e) {
        issueLog.log(e, ErrorType.BatchJob, SubSystem.COM);
    }
}
