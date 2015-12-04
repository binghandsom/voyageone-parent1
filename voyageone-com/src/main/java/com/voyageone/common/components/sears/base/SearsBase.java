package com.voyageone.common.components.sears.base;

import com.voyageone.common.components.sears.bean.OrderResponse;
import com.voyageone.common.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
//        properties.put("javax.net.debug", "ssl");
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

    protected OrderResponse SearsHttpPost(String url, String charset,String content) throws Exception {

        HttpURLConnection http = null;
        OutputStream output = null;
        BufferedReader in = null;
        String ret = null;
        OrderResponse orderResponse = new OrderResponse();

        try {
            URL u = new URL(url);
            http = (HttpURLConnection) u.openConnection();

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
            e.printStackTrace();
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    e.printStackTrace();
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
