package com.voyageone.components.liquifire.service;
import com.voyageone.common.util.HashCodeUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dell on 2016/4/21.
 */
public class LiquidFireClient {
    String url; //LiquidFire url  "http://voyageone.ma.liquifire.com/voyageone"
    String savePath;//生成的图片保存路径  /usr/images
    int connectTimeout = 30 * 1000;
    int readTimeout = 30 * 1000;
    public void LiquidFireClient(String LiquidFireUrl,String savePath) {
        this.setUrl(LiquidFireUrl);
        this.setSavePath(savePath);
    }
    public void LiquidFireClient(String LiquidFireUrl,String savePath,int connectTimeout,int readTimeout) {
        LiquidFireClient(url, savePath);
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
    public String getImage(String param,String fileName) throws Exception {
        String outFilefullName = this.getSavePath() + "/" + fileName + ".jpg";
        String urlParameter = java.net.URLEncoder.encode(param, "UTF-8");
        download(this.getUrl() + "?" + urlParameter, outFilefullName);
        return outFilefullName;
    }
    void download(String urlString, String filename) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(this.getConnectTimeout());
            conn.setReadTimeout(this.getReadTimeout());
            // 输入流
            is = conn.getInputStream();
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
