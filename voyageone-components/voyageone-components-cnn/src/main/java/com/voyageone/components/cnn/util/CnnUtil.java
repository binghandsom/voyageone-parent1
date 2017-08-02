package com.voyageone.components.cnn.util;

import com.voyageone.common.util.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morse on 2017/7/31.
 */
public class CnnUtil {
    private static final String SN_APP_TOKEN = "mVaazc3R85qAU1ZTO3ezxVNtZNbIh4uU4QRtXaR04"; // 暂时写死吧，之后看看是不是放在shop的token_url字段里

    /**
     * 获取sn app请求Header
     */
    public static Map<String, String> getHeadersForSnApp() {
        String accessTimestamp = Long.toString(System.currentTimeMillis());
        String accessSign = MD5.getMD5(SN_APP_TOKEN + accessTimestamp);
        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", SN_APP_TOKEN);
        headers.put("access_timestamp", accessTimestamp);
        headers.put("access_sign", accessSign);
        return headers;
    }
}
