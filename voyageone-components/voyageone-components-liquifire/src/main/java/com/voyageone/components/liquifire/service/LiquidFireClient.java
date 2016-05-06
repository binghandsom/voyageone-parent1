package com.voyageone.components.liquifire.service;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by dell on 2016/4/21.
 */
public class LiquidFireClient {
    //LiquidFire url  "http://voyageone.ma.liquifire.com/voyageone"
    private String url;

    //生成的图片保存路径  /usr/images
    private String savePath;

    private int connectTimeout = 30 * 1000;
    private int readTimeout = 30 * 1000;

    public LiquidFireClient(String LiquidFireUrl, String savePath) {
        this.setUrl(LiquidFireUrl);
        this.setSavePath(savePath);
    }

    public LiquidFireClient(String LiquidFireUrl, String savePath, int connectTimeout, int readTimeout) {
        this(LiquidFireUrl, savePath);
        this.setConnectTimeout(connectTimeout);
        this.setReadTimeout(readTimeout);
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getImage(String param, String fileName, String proxyIP, String proxyPort) throws Exception {
        String outFileFullName = this.getSavePath() + "/" + fileName + ".jpg";
        System.out.println(fileName+":  "+this.getUrl() + "?"+param);
        param = param.replace("\r\n", "");
        param = param.replace("\n", "");
        String urlParameter = java.net.URLEncoder.encode(param, "UTF-8");
        System.out.println(fileName+"encode:  "+this.getUrl() + "?"+urlParameter);
        download(this.getUrl() + "?" + urlParameter, outFileFullName, proxyIP, proxyPort);
        return outFileFullName;
    }

    private void download(String urlString, String filename, String proxyIP, String proxyPort) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection conn;
            if (StringUtils.isEmpty(proxyIP)) {
                conn = (HttpURLConnection) url.openConnection();
            } else {
                // 设置代理 地址和端口
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, Integer.parseInt(proxyPort)));
                conn = (HttpURLConnection) url.openConnection(proxy);
            }

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(this.getConnectTimeout());
            conn.setReadTimeout(this.getReadTimeout());
            // 输入流
            is = conn.getInputStream();
            String lfError= conn.getHeaderField("LF-Error");
            if(!StringUtils.isEmpty(lfError))
            {
                System.out.println("LF-Error"+lfError);
                throw new OpenApiException(ImageErrorEnum.LiquidCreateImageExceptionImage);
            }
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
}
