package com.voyageone.common;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.mail.Mail;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

/**
 * 提供图片服务器的配置
 */
public class ImageServer {

    private final static String MAIN_CODE_ID = "IMAGE_SERVER";
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageServer.class);
    private static String template = null;
    private static String httpTemplate = null;
    private static String exTemplate = null;

    static {
        Class<ImageServer> imageServerClass = ImageServer.class;

        template = getTemplateOrDefault(imageServerClass, "main.template", "%s");
        httpTemplate = getTemplateOrDefault(imageServerClass, "http.template", "HTTP status %s %s, $s");
        exTemplate = getTemplateOrDefault(imageServerClass, "exception.template", "%s: %s<p>%s");
    }

    private static String getTemplateOrDefault(Class<ImageServer> imageServerClass, String name, String defaultValue) {
        final URL httpTemplateUrl = imageServerClass.getResource(name);
        try (InputStream inputStream = httpTemplateUrl.openStream()) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            LOGGER.warn("读取模板 {} 出现错误: {}", name, e);
        }
        return defaultValue;
    }

    /**
     * 上传文件到图片服务
     */
    public static void uploadImage(String channel, String imageName, InputStream inputStream) {
        try {
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("imageName", imageName)
                    .addBinaryBody("file", inputStream, ContentType.DEFAULT_BINARY, "image")
                    .build();

            String uploadApiUrl = imageServerUploadFilePath(channel);
            LOGGER.info("上传图片 {}, 服务地址 {}", imageName, uploadApiUrl);
            HttpResponse response = Request.Post(uploadApiUrl)
                    .body(entity)
                    .execute()
                    .returnResponse();
            StatusLine statusLine = response.getStatusLine();
            int code = statusLine.getStatusCode();
            if (code == 200) {
                return;
            }
            String content = IOUtils.toString(response.getEntity().getContent());
            String reasonPhrase = statusLine.getReasonPhrase();

            final String main = String.format(httpTemplate, code, reasonPhrase, content);
            callTheMaintainer(main);

            throw new FailUploadingException(code, statusLine.getReasonPhrase(), content);

        } catch (Exception e) {

            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            String stackTrace = stackTraceWriter.toString();

            final String name = e.getClass().getName();
            final String main = String.format(exTemplate, name, e.getMessage(), stackTrace);
            callTheMaintainer(main);

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
        return Codes.getCodeName(MAIN_CODE_ID, "domain");
    }

    private static void callTheMaintainer(String message) {

        final String maintainer = Codes.getCodeName(MAIN_CODE_ID, "maintainer");
        if (StringUtils.isBlank(maintainer)) {
            return;
        }

        try {
            final String report = String.format(template, message);
            Mail.send(maintainer, "图片服务异常", report, true);
        } catch (MessagingException e) {
            LOGGER.error("在发送图片服务警告时出现错误", e);
        }
    }

    static class FailUploadingException extends RuntimeException {
        FailUploadingException(Throwable cause) {
            super(cause);
        }

        FailUploadingException(int code, String reasonPhrase, String content) {
            super(String.format("%s %s; %s", code, reasonPhrase, content));
        }
    }
}
