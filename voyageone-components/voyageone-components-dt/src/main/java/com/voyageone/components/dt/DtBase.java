package com.voyageone.components.dt;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;

import java.util.Map;

/**
 * 分销调用基础类
 * <p/>
 * Created by desmond on 2016/12/29.
 */
public abstract class DtBase extends ComponentBase {

    private static final String DEFAULT_API_ACTION = "/onShelfProduct";

    protected String reqApi(ShopBean shop, Map<String, Object> jsonMap) throws Exception {
        return reqApi(shop, JacksonUtil.bean2Json(jsonMap));
    }

    protected String reqApi(ShopBean shop, String apiAction, Map<String, Object> jsonMap) throws Exception {
        return reqApi(shop, apiAction, JacksonUtil.bean2Json(jsonMap));
    }

    protected String reqApi(ShopBean shop, String jsonBody) throws Exception {
        return reqApi(shop, DEFAULT_API_ACTION, jsonBody);
    }

    protected String reqApi(ShopBean shop, String apiAction, String jsonBody) throws Exception {
        return reqApi(shop, apiAction, jsonBody, 3, 1000);
    }

    private String reqApi(ShopBean shopBean, String apiAction, String jsonBody, int tryCount, int tryWait) throws Exception {

        for (int i = 0; i < tryCount; i++) {
            try {
               return HttpExcuteUtils.execute(HttpExcuteUtils.HttpMethod.POST, shopBean.getApp_url().concat(apiAction), jsonBody);
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
