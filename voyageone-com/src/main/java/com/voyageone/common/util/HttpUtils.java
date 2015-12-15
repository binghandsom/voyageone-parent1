package com.voyageone.common.util;

import com.voyageone.common.configs.beans.PostResponse;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpUtils
 *
 * @author Jonas, 5/11/2015. 7/8/2015. 2015-12-11.
 * @version 1.3.0
 * @since 1.0.0
 */
public class HttpUtils {

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
    }

    public static String post(String url, String param) {

        HttpURLConnection connection = null;
        try {
            connection = sendPost(url, param);

            try (InputStream inputStream = connection.getInputStream()) {
                return readConnection(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    public static String post(String url, String param, int connectTimeout, int readTimeout) {

        HttpURLConnection connection = null;
        try {
            connection = sendPost(url, param, connectTimeout, readTimeout);

            try (InputStream inputStream = connection.getInputStream()) {
                return readConnection(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    public static String postWithException(String url, String param) throws IOException {

        HttpURLConnection connection = null;
        try {
            connection = sendPost(url, param);

            try (InputStream inputStream = connection.getInputStream()) {
                return readConnection(inputStream);
            }
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public static String get(String url, String param) {

        if (!StringUtils.isEmpty(param)) url += "?" + param;

        HttpURLConnection connection = null;
        try {
            connection = getConnection(url, "GET");

            connection.connect();

            try (InputStream inputStream = connection.getInputStream()) {
                return readConnection(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return null;
    }

    /**
     * 使用 get 方式，获取输入流
     *
     * @param url   地址
     * @param param 参数
     * @return 输入流
     * @throws IOException
     */
    public static InputStream getInputStream(String url, String param) throws IOException {
        if (!StringUtils.isEmpty(param)) url += "?" + param;

        HttpURLConnection connection = getConnection(url, "GET");

        connection.connect();

        return connection.getInputStream();
    }

    /**
     * 使用 post method 发送 http 请求
     *
     * @param url            请求地址
     * @param param          请求参数
     * @param connectTimeout 尝试打开连接的超时时间
     * @param readTimeout    读取连接内容的超时时间
     * @return http 连接
     * @throws IOException
     */
    private static HttpURLConnection sendPost(String url, String param, int connectTimeout, int readTimeout) throws IOException {

        // 打开和URL之间的连接
        HttpURLConnection connection = getConnection(url, "POST", connectTimeout, readTimeout);

        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 获取URLConnection对象对应的输出流
        try (OutputStream outputStream = connection.getOutputStream();
             PrintWriter printWriter = new PrintWriter(outputStream)) {

            // 发送请求参数
            printWriter.print(param);
            printWriter.flush();
        }

        connection.connect();

        return connection;
    }

    /**
     * 使用 post method 发送 http 请求, 超时配置默认为 60000, 120000
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return http 连接
     * @throws IOException
     */
    private static HttpURLConnection sendPost(String url, String param) throws IOException {
        return sendPost(url, param, 60000, 120000);
    }

    /**
     * 以默认的 UTF－8 读取结果
     *
     * @param inputStream 连接的输入流
     * @return 解码的字符串
     * @throws IOException
     */
    private static String readConnection(InputStream inputStream) throws IOException {

        return readConnection(inputStream, "UTF-8");
    }

    /**
     * 从连接的输入流里读取结果
     *
     * @param inputStream 返回的输入流
     * @param encoding    编码方式
     * @return 解码的字符串
     * @throws IOException
     */
    private static String readConnection(InputStream inputStream, String encoding) throws IOException {

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) result.append(line);

            return result.toString();
        }
    }

    /**
     * 获取一个还没有打开的连接
     *
     * @param location 需要打开的地址
     * @param method   http 连接的方法 GET/POST
     * @return HttpURLConnection
     * @throws IOException
     */
    private static HttpURLConnection getConnection(String location, String method) throws IOException {

        return getConnection(location, method, 10000, 10000);
    }

    private static HttpURLConnection getConnection(String location, String method, int connectTimeout, int readTimeout) throws IOException {

        URL url = new URL(location);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        //设置连接超时
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        // 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        return connection;
    }

    public static String PostSoap(String postUrl, String soap_action, String send_soap) throws Exception {
        String ret;
        URL url = new URL(postUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(30000);
        // 设置从主机读取数据超时（单位：毫秒）
        http.setReadTimeout(30000);
        http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
        http.setRequestProperty("Content-Length", String.valueOf(send_soap.length()));
        http.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//	        _httpConn.setRequestProperty("soapActionString",_soap_action_str);
        http.setRequestProperty("SOAPAction", soap_action);
        http.setRequestMethod("POST");
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setUseCaches(false);

        http.connect();

        OutputStream out = http.getOutputStream();
        out.write(send_soap.getBytes());
        out.flush();
        out.close();

        int code = http.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            in.close();
            ret = sb.toString();
        } else {
            throw new Exception("Request 异常  Response Code: " + String.valueOf(code));
        }
        return ret;

    }

    /*
     * 带Session的HttpPost请求
     */
    public static PostResponse PostSoapFull(String postUrl, String soap_action, String send_soap, String session) throws Exception {

        URL url = new URL(postUrl);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(30000);

        // 设置从主机读取数据超时（单位：毫秒）
        http.setReadTimeout(30000);
        http.setRequestProperty("Content-Length", String.valueOf(send_soap.length()));
        http.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        http.setRequestProperty("SOAPAction", soap_action);
        if (session != null && !"".equals(session)) {
            http.setRequestProperty("Cookie", session);
        }

        http.setRequestMethod("POST");
        http.setDoInput(true);
        http.setDoOutput(true);
        http.setUseCaches(false);

        PostResponse res = new PostResponse();

        OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
        out.append(send_soap);
        out.flush();
        out.close();

        int code = http.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            http.disconnect();

            //获取 sessionid
            String sessionId = http.getHeaderField("Set-Cookie");
            if (sessionId != null && !"".equals(sessionId)) {
                sessionId = sessionId.substring(0, sessionId.indexOf(";"));
                res.setSession(sessionId);
            }
            res.setResult(buffer.toString());
        } else {
            throw new Exception("Request 异常  Response Code: " + String.valueOf(code));
        }
        return res;

    }
}
