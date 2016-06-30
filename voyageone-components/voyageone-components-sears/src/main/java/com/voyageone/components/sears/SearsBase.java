package com.voyageone.components.sears;

import com.voyageone.common.util.HttpUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.sears.bean.OrderResponse;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SearsBase extends ComponentBase {

    public static final int C_MAX_API_REPEAT_TIME = 3;

      // 本地测试时请用Test后缀的文件
//    public final String keystore = getClass().getResource("/keystore_test.jks").getPath();
//    public final String keystore_password = "password";
    public final String keystore = "/opt/app-shared/voyageone_web/contents/other/third_party/013/api_key/keystore.jks";
    public final String keystore_password = "tmall611";

    // 本地测试时请用Test后缀的文件
//    public final String trustStore = getClass().getResource("/truststore_test.jks").getPath();
//    public final String trustStore_password = "password";
    public final String trustStore = "/opt/app-shared/voyageone_web/contents/other/third_party/013/api_key/truststore.jks";
    public final String trustStore_password = "tmall611";

    public final Properties properties;

    public final String searsUrl = "https://api.b2b.sears.com/v/2/";

    public final String searsOrderUrlByOrderId = searsUrl + "/orders/id/";

    public final String searsOrderByOrderReferenceUrl = searsUrl + "/orders/order_reference/";

    public final String updateStatusUrl = searsUrl + "orders/id/%s/update";

    public SearsBase(){
        properties = new Properties();
        properties.put("javax.net.ssl.keyStorePassword", keystore_password);
        properties.put("javax.net.ssl.keyStoreType", "jks");
        properties.put("javax.net.ssl.trustStore", trustStore);
        properties.put("javax.net.ssl.trustStoreType", "jks");
        properties.put("javax.net.ssl.trustStorePassword", trustStore_password);
    }

    /**
     * 调用Sears的webService
     */
    protected String reqSearsApi(String postUrl) throws Exception {

//        logger.info("keystore path: "+keystore);
        // Properties属性没有设置的场合 Properties的SSL属性设置
//        if (!System.getProperties().contains(keystore)){
//            System.getProperties().putAll(properties);
//        }
        return HttpUtils.get(postUrl, trustStore, trustStore_password, keystore_password);

    }

    /**
     * 调用Sears的webService
     */
    protected String reqSearsApi(String postUrl, String param) throws Exception {

//        logger.info("keystore path: "+keystore);
        // Properties属性没有设置的场合 Properties的SSL属性设置
//        if (!System.getProperties().contains(keystore)){
//            System.getProperties().putAll(properties);
//        }
        return HttpUtils.get(postUrl,param, trustStore, trustStore_password, keystore_password);
//        return HttpUtils.get(postUrl,param);

    }

    /**
     * 超时的话，自动重调
     */
    protected String reqSearsOnTimeoutRepert(String postUrl, String param) throws Exception {


        for (int intApiErrorCount = 0; intApiErrorCount < C_MAX_API_REPEAT_TIME; intApiErrorCount++) {
            try {
                return HttpUtils.get(postUrl, param, trustStore, trustStore_password, keystore_password);
            } catch (Exception e) {
                // 最后一次出错则直接抛出
                Thread.sleep(1000);
                if (C_MAX_API_REPEAT_TIME - intApiErrorCount == 1) throw e;
            }
        }
        return "";
    }

    protected OrderResponse searsHttpPost(String url, String charset, String content) throws Exception {

        // Properties属性没有设置的场合 Properties的SSL属性设置
//        if (!System.getProperties().contains(keystore)){
//            System.getProperties().putAll(properties);
//        }

        HttpsURLConnection http = null;
        OutputStream output = null;
        BufferedReader in = null;
        OrderResponse orderResponse = new OrderResponse();

        SSLSocketFactory ssf = HttpUtils.getSsf(trustStore, trustStore_password, keystore_password);

        try {
            URL u = new URL(url);
            http = (HttpsURLConnection) u.openConnection();

            if (ssf != null) {
                http.setSSLSocketFactory(ssf);
            }

            http.setDoOutput(true);
            http.setDoInput(true);
            // 设置连接主机超时（单位：毫秒）

            http.setConnectTimeout(10000);
            // 设置从主机读取数据超时（单位：毫秒）
            http.setReadTimeout(10000);
            // 设定请求的方法为"POST"，默认是GET
            http.setRequestMethod("POST");
            // 设定传送的内容类型
            http.setRequestProperty("Content-Type", "text/xml");
            http.connect();
            output = http.getOutputStream();
            if (charset != null) {
                output.write(content.getBytes(charset));
            } else {
                output.write(content.getBytes());
            }
            output.flush();
            output.close();

            int code = http.getResponseCode();

            StringBuilder sb = new StringBuilder();
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端

            if (charset != null) {
                in = new BufferedReader(new InputStreamReader(code >= 400?http.getErrorStream():http.getInputStream(), charset));
            } else {
                in = new BufferedReader(new InputStreamReader(code >= 400?http.getErrorStream():http.getInputStream()));
            }

            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            in.close();
            http.disconnect();
            if(code >= 400){
                orderResponse.setMessage(sb.toString());
            }else{
                String orderId = http.getHeaderField("Location").substring(http.getHeaderField("Location").lastIndexOf("/")+1);
                orderResponse.setOrderId(orderId);
                orderResponse.setMessage("Succeed");
            }
        } catch (IOException e) {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ignored) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (http != null) {
                http.disconnect();
            }
            // 异常发生
            throw new Exception("HttpPost fail "+e.getMessage());
        }
        return orderResponse;
    }
}
