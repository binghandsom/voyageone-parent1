package com.voyageone.common.util.baidu.translate;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by jacky on 2015/11/19.
 */
public class BaiduTranslateUtil {

    protected final static Logger logger = LoggerFactory.getLogger(BaiduTranslateUtil.class);

    // 百度翻译设置
    private static final String BAIDU_TRANSLATE_CONFIG = "BAIDU_TRANSLATE_CONFIG";

    public static List<String> translate(List<String> query) throws Exception {
        List<String> resultStrList = new ArrayList<String>();

        // 百度翻译URL
        String url = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "Url");
        if (!url.endsWith("?")) {
            url += "?";
        }
        // APP ID
        String appId = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "AppId");
        // 密钥
        String secretKey = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "SecretKey");
        // 翻译源语言
        String from = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "From");
        // 译文语言
        String to = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "To");
        // 随机数
        int random = new Random().nextInt(89999999) + 10000000;
        String salt = String.valueOf(random);

        // 请求翻译query
        StringBuilder q = new StringBuilder();
        StringBuilder qSign = new StringBuilder();
        if (query != null && !query.isEmpty()) {
            String qSplit = URLEncoder.encode("\n", "utf-8");
            String qSplitSign = "\n";
            int qSize = query.size();
            for (int i = 0; i < qSize; i++) {
                if (i == 0) {
                    q.append(URLEncoder.encode(query.get(i), "utf-8"));

                    qSign.append(query.get(i));
                } else {
                    q.append(qSplit);
                    q.append(URLEncoder.encode(query.get(i), "utf-8"));

                    qSign.append(qSplitSign);
                    qSign.append(query.get(i));
                }
            }
        } else {
            return resultStrList;
        }

        // 签名
        StringBuilder signBuilder = new StringBuilder();
        signBuilder.append(appId);
        signBuilder.append(qSign);
        signBuilder.append(salt);
        signBuilder.append(secretKey);
        String sign = MD5.getMD5(signBuilder.toString());

        // 请求url拼接
        StringBuilder param = new StringBuilder();
        param.append("q=");
        param.append(q.toString());
        param.append("&");
        param.append("appid=");
        param.append(appId);
        param.append("&");
        param.append("salt=");
        param.append(salt);
        param.append("&");
        param.append("from=");
        param.append(from);
        param.append("&");
        param.append("to=");
        param.append(to);
        param.append("&");
        param.append("sign=");
        param.append(sign);

        String transResult = HttpUtils.post(url, param.toString());

        Map<String, Object> jsonToMap = JsonUtil.jsonToMap(transResult);
        // 百度翻译API服务发生错误
        if (jsonToMap.containsKey("error_code")) {
            Object error_code = jsonToMap.get("error_code");
            Object error_msg = jsonToMap.get("error_msg");

            logger.info("百度翻译API服务发生错误。error_code：" + error_code + " error_msg：" + error_msg);

            throw new Exception("百度翻译API服务发生错误：" + jsonToMap);

        } else {
            Object trans_result = jsonToMap.get("trans_result");
            List<Map<String, String>> mapList = (List<Map<String, String>>) trans_result;
            if (mapList != null && !mapList.isEmpty()) {
                for (Map<String, String> map : mapList) {
                    String src = map.get("src");
                    String dst = map.get("dst");
                    logger.info("src:" + src + " -> dst:" + dst);
                    resultStrList.add(dst);
                }
            }
        }

        return resultStrList;

    }

    public static String translate(String query) throws Exception {
        List<String> strings = translate(Arrays.asList(query));
        if(!ListUtils.isNull(strings)){
            return strings.get(0);
        }
        return "";
    }
}
