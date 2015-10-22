package com.voyageone.common.components.sears.base;

import com.voyageone.common.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SearsBase {

    protected Log logger = LogFactory.getLog(getClass());

    public static final int C_MAX_API_REPEAT_TIME = 3;

    public final String keystore = getClass().getResource("/keystore.jks").getPath();

    public final String trustStore = getClass().getResource("/truststore.jks").getPath();

    public final Properties properties;

    public final String searsUrl = "https://api.b2b.sears.com/v/2/";

    public SearsBase(){
        properties = new Properties();
        properties.put("javax.net.debug", "ssl");
        properties.put("javax.net.ssl.keyStore", keystore);
        properties.put("javax.net.ssl.keyStorePassword", "password");
        properties.put("javax.net.ssl.keyStoreType", "jks");
        properties.put("javax.net.ssl.trustStore", trustStore);
        properties.put("javax.net.ssl.trustStoreType", "jks");
        properties.put("javax.net.ssl.trustStorePassword", "password");
    }
    /**
     * 调用Sears的webService
     * @param postUrl
     * @param param
     * @return
     * @throws Exception
     */
    protected String reqSearsApi(String postUrl, String param) throws Exception {

        // Properties属性没有设置的场合 Properties的SSL属性设置
        if (!System.getProperties().contains(keystore)){
            System.getProperties().putAll(properties);
        }
        return HttpUtils.get(postUrl,param);

    }

    /**
     * 超时的话，自动重调
     * @param postUrl
     * @param param
     * @return
     * @throws Exception
     */
    protected String reqSearsOnTimeoutRepert(String postUrl, String param) throws Exception {


        for (int intApiErrorCount = 0; intApiErrorCount < C_MAX_API_REPEAT_TIME; intApiErrorCount++) {
            try {
                return HttpUtils.get(postUrl, param);
            } catch (Exception e) {
                // 最后一次出错则直接抛出
                Thread.sleep(1000);
                if (C_MAX_API_REPEAT_TIME - intApiErrorCount == 1) throw e;
            }
        }
        return "";
    }
}
