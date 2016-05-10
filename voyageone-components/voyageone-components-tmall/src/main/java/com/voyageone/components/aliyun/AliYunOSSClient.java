package com.voyageone.components.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class AliYunOSSClient {
    String endpoint;// = "http://oss-cn-shenzhen.aliyuncs.com/";//http://oss-cn-hangzhou.aliyuncs.com";
    String accessKeyId;// = "v5l02zcFVl6rBKXg";
    String accessKeySecret;// = "c6VxvbVsDLZI4vAePHn6PxsEAuQGuq";
    public String getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    public String getAccessKeyId() {
        return accessKeyId;
    }
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
    public  AliYunOSSClient( String endpoint,String accessKeyId,String accessKeySecret) {
        this.setEndpoint(endpoint);
        this.setAccessKeyId(accessKeyId);
        this.setAccessKeySecret(accessKeySecret);
    }
    OSSClient create()
    {
//        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com/";//http://oss-cn-hangzhou.aliyuncs.com";
//        String accessKeyId = "v5l02zcFVl6rBKXg";
//        String accessKeySecret = "c6VxvbVsDLZI4vAePHn6PxsEAuQGuq";
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        return client;
    }
    //上传  String channelId, String size,int templateId, String fileName
    /**
     *
     * @param filefullPath  本地文件路径
     * @param bucketName   阿里云oss 存储区
     * @param fileOSSFullPath   阿里云oss文件路径
     * @return
     * @throws FileNotFoundException
     */
    public String putOSS(String filefullPath,String bucketName,String fileOSSFullPath) throws FileNotFoundException {
        OSSClient client = null;
        try {
            client = create();
            final String keySuffixWithSlash = fileOSSFullPath;//"products/" + channelId + "/" + size + "/" + Integer.toString(templateId) + "/" + fileName;
            File file = new File(filefullPath);
            FileInputStream inputStream = new FileInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            // 获取指定文件的输入流
            InputStream content = new FileInputStream(file);//sdk内部会关
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());
            // String bucketName = "shenzhen-vo";
            PutObjectResult result = client.putObject(bucketName, keySuffixWithSlash, inputStream, meta);
            return result.toString();
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }
}
