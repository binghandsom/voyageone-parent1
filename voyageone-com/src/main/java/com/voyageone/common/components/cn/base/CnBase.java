package com.voyageone.common.components.cn.base;

import com.google.gson.Gson;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对独立域名提供接口调用基础
 *
 * Created by Jonas on 7/8/15.
 */
public abstract class CnBase {

    @Autowired
    protected IssueLog issueLog;

    protected Log logger = LogFactory.getLog(getClass());

    protected String post(String apiAction, Object parameter, ShopBean shopBean) {

        return post(apiAction, parameter, 3, 1000, shopBean);
    }

    private String post(String apiAction, Object parameter, int tryCount, int tryWait, ShopBean shopBean) {

        String json = new Gson().toJson(parameter);

        for (int i = 0; i < tryCount; i++) {
            try {
                return HttpUtils.post(shopBean.getApp_url() + apiAction, json);
            } catch (RuntimeException e) {
                if (tryCount - i == 1) throw e;
                try {
                    Thread.sleep(tryWait);
                } catch (InterruptedException ignore) {
                }
            }
        }

        return null;
    }
}
