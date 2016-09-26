package com.voyageone.common.util;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

/**
 * HttpExcuteUtils
 *
 * @author aooer 2016/9/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public class HttpExcuteUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpExcuteUtils.class);

    /************************
     * 通用execute方法
     *********************/
    public enum HttpMethod {
        GET, POST, PUT, PATCH, DELETE
    }

    //简单请求方法
    public static String execute(HttpMethod method, String url) throws Exception {
        return execute(method, url, null);
    }

    //可以设置body的请求方法
    public static String execute(HttpMethod method, String url, String jsonBody) throws Exception {
        return execute(method, url, jsonBody, null);
    }

    //可以设置headers的请求方法
    public static String execute(HttpMethod method, String url, String jsonBody, Map<String, String> headers) throws Exception {
        return execute(method, url, jsonBody, headers, null);
    }

    //可以设置代理ip的请求方法
    public static String execute(HttpMethod method, String url, String jsonBody, Map<String, String> headers, HttpHost proxyhttpHost) throws Exception {
        return execute(method, url, jsonBody, headers, proxyhttpHost, null);
    }

    //可以设置cookie和代理ip的请求方法
    public static String execute(HttpMethod method, String url, String jsonBody, Map<String, String> headers, HttpHost proxyhttpHost, CookieStore cookieStore) throws Exception {
        return execute(method, url, jsonBody, ContentType.APPLICATION_JSON, headers, proxyhttpHost, cookieStore);
    }


    /**
     * 通用http访问
     * 支持常见的http请求
     *
     * @param method        请求类型
     * @param url           请求地址
     * @param strBody       请求body
     * @param bodyType      body类型 设置contentType
     * @param headers       请求头信息
     * @param proxyhttpHost 代理地址
     * @param cookieStore   cookie设置
     * @return 响应
     * @throws Exception
     */
    private static String execute(HttpMethod method, String url, String strBody, ContentType bodyType, Map<String, String> headers, HttpHost proxyhttpHost, CookieStore cookieStore) throws Exception {
        HttpRequestBase request = null;

        // match method
        if (method.equals(HttpMethod.GET)) request = new HttpGet(new URI(url));
        if (method.equals(HttpMethod.DELETE)) request = new HttpDelete(new URI(url));
        if (method.equals(HttpMethod.POST)) request = new HttpPost(new URI(url));
        if (method.equals(HttpMethod.PATCH)) request = new HttpPatch(new URI(url));
        if (method.equals(HttpMethod.PUT)) request = new HttpPut(new URI(url));

        // assert
        Assert.notNull(request, "Http请求方法不合法");

        // set headers
        if (!MapUtils.isEmpty(headers))
            for (Map.Entry<String, String> e : headers.entrySet()) request.setHeader(e.getKey(), e.getValue());

        // set body
        if (!StringUtils.isEmpty(strBody) && request instanceof HttpEntityEnclosingRequestBase)
            ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(strBody, bodyType));

        // set proxy
        if (proxyhttpHost != null) request.setConfig(RequestConfig.custom().setProxy(proxyhttpHost).build());

        // execute
        CloseableHttpClient httpclient = HttpClients.createDefault();

        if (cookieStore != null) {
            httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore).build();
        }

        HttpResponse response = httpclient.execute(request);

        // assert
        Assert.notNull(response.getEntity(), "Http响应内容为空");
        // build response
        BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 10 * 1024);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null)
            sb.append(line);

        // close
        buffer.close();
        return sb.toString();
    }
}
