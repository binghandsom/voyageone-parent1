package com.voyageone.common;

import com.voyageone.common.configs.Codes;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * 提供图片服务器的配置
 */
public class ImageServer {

    private static ImageServer imageServer;

    private static Logger logger = LoggerFactory.getLogger(ImageServer.class);

    private String domain;

    @PostConstruct
    public void afterPropertiesSet() {
        String domain = Codes.getCodeName("IMAGE_SERVER", "domain");
        if (StringUtils.isNotBlank(domain)) {
            this.domain = domain;
            return;
        }
        this.domain = "image.voyageone.com.cn";
        logger.warn("没有从 Codes 配置中获取到图片服务的配置，将使用默认值 {}", this.domain);
    }


    /**
     * 上传文件到图片服务
     */
    public static void uploadImage(String channel, String imageName, InputStream inputStream) {
        HttpEntity entity = MultipartEntityBuilder.create()
                .addTextBody("imageName", imageName)
                .addBinaryBody("file", inputStream, ContentType.DEFAULT_BINARY, "image")
                .build();
        try {
            HttpResponse response = Request.Post(imageServerUploadFilePath(channel))
                    .body(entity)
                    .execute()
                    .returnResponse();
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                return;
            }
            String content = IOUtils.toString(response.getEntity().getContent());
            throw new FailUploadingException(code, statusLine.getReasonPhrase(), content);
        } catch (IOException e) {
            throw new FailUploadingException(e);
        }
    }

    /**
     * 获取经过包装的 s7 图片的主题路径部分
     */
    public static String imageBasePath(String channel) {
        return imageServerUrl(channel, "is/image/sneakerhead/");
    }

    public static String imageUrl(String channel, String imageName) {
        return imageBasePath(channel) + imageName;
    }

    public static String imageServerUrl(String path) {
        return "http://" + domain() + "/" + path;
    }

    private static String imageServerUploadFilePath(String channel) {
        return imageServerUrl(channel, "file");
    }

    private static String imageServerUrl(String channel, String path) {
        return "http://" + domain() + "/" + channel + "/" + path;
    }

    /**
     * 获取当前配置的图片服务器地址
     */
    private static String domain() {
        if (imageServer == null) {
            throw new TooEarlyException();
        }

        return imageServer.domain;
    }

    public static class TooEarlyException extends RuntimeException {
        TooEarlyException() {
            super("尝试获取图片服务的域名太早了。Spring 还没有初始化 ImageServer 的示例");
        }
    }

    public static class FailUploadingException extends RuntimeException {
        FailUploadingException(Throwable cause) {
            super(cause);
        }

        FailUploadingException(int code, String reasonPhrase, String content) {
            super(String.format("%s %s; %s", code, reasonPhrase, content));
        }
    }

    /**
     * 自动初始化一个 ImageServer 的实例，并保存到静态单例变量中
     */
    @Configuration
    public static class ImageServerAutoConfiguration {
        @Bean
        public ImageServer imageServer() {
            return imageServer = new ImageServer();
        }
    }
}

