package com.voyageone.common;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.mail.Mail;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供图片服务器的配置
 */
public class ImageServer {

    private final static String MAIN_CODE_ID = "IMAGE_SERVER";
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageServer.class);
    private static String template = null;
    private static String httpTemplate = null;
    private static String exTemplate = null;
    private static String proxyTemplate = null;

    static {
        Class<ImageServer> imageServerClass = ImageServer.class;

        template = getTemplateOrDefault(imageServerClass, "main.template", "%s");
        httpTemplate = getTemplateOrDefault(imageServerClass, "http.template", "HTTP status %s %s, %s");
        exTemplate = getTemplateOrDefault(imageServerClass, "exception.template", "%s: %s<p>%s");
        proxyTemplate = getTemplateOrDefault(imageServerClass, "proxy.template", "%s<p>%s");
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
            byte[] byteArray = IOUtils.toByteArray(inputStream);

            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("imageName", imageName)
                    .addBinaryBody("file", byteArray, ContentType.DEFAULT_BINARY, "image")
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

            final String main = String.format(httpTemplate, code, reasonPhrase, imageName, byteArray.length,
                    content);

            sendUploadFailNotify(main);

            throw new FailUploadingException(code, statusLine.getReasonPhrase(), content);

        } catch (FailUploadingException fe) {

            throw fe;

        } catch (Exception e) {

            sendUploadFailNotify(buildExceptionMail(e));

            throw new FailUploadingException(e);
        }
    }

    /**
     * 获取渠道的图片基础路径
     *
     * @return http://{domain}/{channel}/is/image/sneakerhead/
     */
    public static String imageBasePath(String channel) {
        return imageServerUrl(channel, "is/image/sneakerhead/");
    }

    /**
     * 根据图片名，返回完整的图片路径
     *
     * @return http://{domain}/{channel}/is/image/sneakerhead/{imageName}
     */
    public static String imageUrl(String channel, String imageName) {
        return imageBasePath(channel) + imageName;
    }

    /**
     * 返回带有域名的地址
     *
     * @return http://{domain}/{path}
     */
    public static String imageServerUrl(String path) {
        return "http://" + domain() + "/" + path;
    }

    /**
     * 返回用于前端使用的图片地址模板，地址包含两个用于替换的部分，即 {channel} 和 {image_name}
     *
     * @return http://{domain}/\{channel\}/is/image/sneakerhead/\{image_name\}
     */
    public static String frontendImageUrlTemplate() {
        return imageUrl("{channel}", "{image_name}");
    }

    /**
     * 替换 {@code imageUrl} 的域名，指向 Image Server，并代理下载
     */
    public static InputStream proxyDownloadImage(String imageUrl, String channel) throws IOException {

        // 如果传入的地址符合 ImageServer 的请求地址，那么就不需要替换并代理了
        // 所以直接处理下载
        if (imageUrl.startsWith(imageServerUrl(channel))) {
            return new URL(imageUrl).openStream();
        }

        // use                https?://.+?:?\d*?/(.+)$
        // match              http://xxx.xxx.xxx/is/image.....
        // get groupValue(1)  is/image.....
        final Pattern pattern = Pattern.compile("^https?://.+?:?\\d*?/(.+)$");
        final Matcher matcher = pattern.matcher(imageUrl);

        // 不能匹配，那就算了，提出警告，并直接代理
        if (!matcher.matches()) {
            sendNotifyForDownload(String.format(proxyTemplate, pattern.pattern(), imageUrl));
            return httpGet(imageUrl);
        }

        final String path = matcher.group(1);
        final String ISImageUrl = imageServerUrl(channel, path);

        try {
            return httpGet(ISImageUrl);
        } catch (IOException e) {
            sendNotifyForDownload(buildExceptionMail(e));
            throw e;
        }
    }

    private static InputStream httpGet(String imageUrl) throws IOException {
        return Request.Get(imageUrl).execute().returnContent().asStream();
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

    private static void sendNotifyForDownload(String message) {
        callTheMaintainer(message, "下载");
    }

    private static void sendUploadFailNotify(String message) {
        callTheMaintainer(message, "上载");
    }

    private static void callTheMaintainer(String message, String subTitle) {

        final String maintainer = Codes.getCodeName(MAIN_CODE_ID, "maintainer");
        if (StringUtils.isBlank(maintainer)) {
            return;
        }

        try {
            final String report = String.format(template, subTitle, message);
            Mail.send(maintainer, "图片服务异常", report, true);
        } catch (MessagingException e) {
            LOGGER.error("在发送图片服务警告时出现错误", e);
        }
    }

    private static String buildExceptionMail(Exception e) {
        final String name = e.getClass().getName();
        final String stackTrace = ExceptionUtils.getStackTrace(e);
        return String.format(exTemplate, name, e.getMessage(), stackTrace);
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
