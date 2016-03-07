package com.voyageone.task2.cms.utils;

import com.voyageone.common.util.HttpUtils;

/**
 * @author Jonas. 2015-12-21.
 */
public class WebServiceUtil {
    /**
     * 通过 HTTP POST 提交 JSON 数据
     *
     * @param url      请求地址
     * @param jsonData JSON 数据
     * @return 响应的内容
     */
    public static String postByJsonStr(String url, String jsonData) {
        return HttpUtils.post(url, jsonData, 60000, 120000);
    }
}
