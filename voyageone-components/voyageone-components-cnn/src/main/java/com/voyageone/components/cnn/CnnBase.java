package com.voyageone.components.cnn;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.ComponentConstants;
import com.voyageone.components.cnn.request.AbstractCnnRequest;
import com.voyageone.components.cnn.request.CnnUrlRequest;
import com.voyageone.components.cnn.response.AbstractCnnResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 新独立域名调用基础类
 * <p/>
 * Created by desmond on 2017/01/04.
 */
public abstract class CnnBase extends ComponentBase {

    private static final String SN_APP_TOKEN = "mVaazc3R85qAU1ZTO3ezxVNtZNbIh4uU4QRtXaR04"; // 暂时写死吧，之后看看是不是放在shop的token_url字段里

    protected <T extends AbstractCnnResponse> T reqApi(ShopBean shop, AbstractCnnRequest<T> request) throws Exception {
        String apiAction = request.getUrl();
        String accessTimestamp = Long.toString(System.currentTimeMillis());
        String accessSign = MD5.getMd5_16(SN_APP_TOKEN + accessTimestamp);
        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", SN_APP_TOKEN);
        headers.put("access_timestamp", accessTimestamp);
        headers.put("access_sign", accessSign);
        logger.info(request.getClass().getSimpleName() + " request info:" + request);
        String jsonRes;
        if (request instanceof CnnUrlRequest) {
            List<String> params = ((CnnUrlRequest) request).getParams();
            for (String param : params) {
//            Pattern pattern = Pattern.compile("[{][^/]*[}]");
                Pattern pattern = Pattern.compile("\\{[^/]*\\}");
                apiAction = pattern.matcher(apiAction).replaceFirst(param);
            }
            jsonRes = reqApi(shop, apiAction, null, ComponentConstants.C_MAX_API_ERROR, ComponentConstants.C_CONNECT_TIMEOUT, headers);
        } else {
            jsonRes = reqApi(shop, apiAction, request.toString(), ComponentConstants.C_MAX_API_ERROR, ComponentConstants.C_CONNECT_TIMEOUT, headers);
        }
        logger.info(request.getClass().getSimpleName() + " response info:" + jsonRes);
        return request.getResponse(jsonRes);
    }

    protected String reqApi(ShopBean shop, String apiAction, Map<String, Object> jsonMap) throws Exception {
        return reqApi(shop, apiAction, JacksonUtil.bean2Json(jsonMap));
    }

    protected String reqApi(ShopBean shop, String apiAction, String jsonBody) throws Exception {
        return reqApi(shop, apiAction, jsonBody, ComponentConstants.C_MAX_API_ERROR, ComponentConstants.C_CONNECT_TIMEOUT);
    }

    protected String reqApi(ShopBean shopBean, String apiAction, String jsonBody, int tryCount, int tryWait) throws Exception {
        return reqApi(shopBean, apiAction, jsonBody, tryCount, tryWait, null);
    }

    private String reqApi(ShopBean shopBean, String apiAction, String jsonBody, int tryCount, int tryWait, Map<String, String> headers) throws Exception {

        for (int i = 0; i < tryCount; i++) {
            try {
               return HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, shopBean.getApp_url().concat(apiAction), jsonBody, headers);
            } catch (RuntimeException e) {
                if (tryCount - i == 1) throw e;
                try {
                    Thread.sleep(tryWait);
                } catch (Exception ignore) {
                }
            }
        }

        return null;
    }

}
