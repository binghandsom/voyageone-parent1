package com.voyageone.components.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AliYunOSSClient {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

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

    public AliYunOSSClient(String endpoint, String accessKeyId, String accessKeySecret) {
        this.setEndpoint(endpoint);
        this.setAccessKeyId(accessKeyId);
        this.setAccessKeySecret(accessKeySecret);
    }

    private OSSClient create() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * @param filefullPath    本地文件路径
     * @param bucketName      阿里云oss 存储区
     * @param fileOSSFullPath 阿里云oss文件路径
     * @return String
     * @throws FileNotFoundException
     */
    public String putOSS(String filefullPath, String bucketName, String fileOSSFullPath) throws FileNotFoundException {
        OSSClient client = null;
        FileInputStream inputStream = null;
        try {
            client = create();
            File file = new File(filefullPath);
            // 获取指定文件的输入流
            inputStream = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());
            // String bucketName = "shenzhen-vo";
            PutObjectResult result = client.putObject(bucketName, fileOSSFullPath, inputStream, meta);
            return result.toString();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }
            if (client != null) {
                client.shutdown();
            }
        }
    }
}
