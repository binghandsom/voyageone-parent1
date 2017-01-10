package com.voyageone.components.cnn;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpExcuteUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;

import java.util.Map;

/**
 * 新独立域名调用基础类
 * <p/>
 * Created by desmond on 2017/01/04.
 */
public abstract class CnnBase extends ComponentBase {

    protected String reqApi(ShopBean shop, String apiAction, Map<String, Object> jsonMap) throws Exception {
        return reqApi(shop, apiAction, JacksonUtil.bean2Json(jsonMap));
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
