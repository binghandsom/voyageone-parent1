package com.voyageone.common.util;

import com.voyageone.common.configs.Codes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Adobe Http Image Upload Util
 *
 * @author chuanyu.liang, 2016/07/12
 * @version 1.3.0
 * @since 1.0.0
 *
 * INSERT INTO `tm_code` VALUES ('S7HTTP_CONFIG', 'Url', 'https://s7sps1ssl.scene7.com/scene7/UploadFile', '', 'Scene7HTTP设置：url');
 * INSERT INTO `tm_code` VALUES ('S7HTTP_CONFIG', 'UserName', 'admin@voyageone.com', '', 'Scene7HTTP设置：用户名');
 * INSERT INTO `tm_code` VALUES ('S7HTTP_CONFIG', 'Password', '2016Dirzh#1', '', 'Scene7HTTP设置：密码');
 */
public class HttpScene7 {
    private final static Logger logger = LoggerFactory.getLogger(HttpScene7.class);

    private final static String S7HTTP_CONFIG = "S7HTTP_CONFIG";
    private static final String NAME_FILENAME = "Filename";
    private static final String NAME_AUTH = "auth";
    private static final String NAME_UPLOADPARAMS = "uploadParams";
    private static final String NAME_UPLOAD = "Upload";
    private static final String NAME_FILE = "file";
    private static final String PATH_PRE = "sneakerhead/";
    private static final String DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss";

    private final static String VALUE_AUTH =
            "<authHeader xmlns=\"http://www.scene7.com/IpsApi/xsd/2013-08-29-beta\">" +
                    "<user>%s</user>" +
                    "<password>%s</password>" +
                    "<locale>en</locale>" +
                    "<appName>Adobe.Scene7.SPS</appName>" +
                    "<appVersion>6.10-194347</appVersion>" +
                    "<faultHttpStatusCode>200</faultHttpStatusCode>" +
                    "</authHeader>";

    private final static String VALUE_UPLOAD_PARAMS =
            "<uploadPostParam xmlns=\"http://www.scene7.com/IpsApi/xsd/2013-08-29-beta\">" +
                    "<companyHandle>c|8553</companyHandle>" +
                    "<jobName>Add_%s</jobName>" +
                    "<locale>en</locale>" +
                    "<description>%s</description>" +
                    "<destFolder>%s</destFolder>" +
                    "<fileName>%s</fileName>" +
                    "<endJob>true</endJob>" +
                    "<uploadParams>" +
                    "<overwrite>true</overwrite>" +
                    "<readyForPublish>true</readyForPublish>" +
                    "<preservePublishState>false</preservePublishState>" +
                    "<createMask>false</createMask>" +
                    "<preserveCrop>true</preserveCrop>" +
                    "<photoshopOptions><process>MaintainLayers</process><layerOptions><layerNaming>LayerName</layerNaming><anchor>Center</anchor><createTemplate>true</createTemplate><extractText>true</extractText><extendLayers>false</extendLayers></layerOptions></photoshopOptions>" +
                    "<postScriptOptions><process>Rasterize</process><resolution>150</resolution><colorspace>Auto</colorspace><alpha>false</alpha><extractSearchWords>true</extractSearchWords></postScriptOptions>" +
                    "<pdfOptions><process>Rasterize</process><resolution>150</resolution><colorspace>Auto</colorspace><pdfCatalog>true</pdfCatalog><extractSearchWords>true</extractSearchWords><extractLinks>true</extractLinks></pdfOptions>" +
                    "<illustratorOptions><process>Rasterize</process><resolution>150</resolution><colorspace>Auto</colorspace><alpha>false</alpha></illustratorOptions>" +
                    "<colorManagementOptions><colorManagement>Default</colorManagement></colorManagementOptions>" +
                    "<autoSetCreationOptions/>" +
                    "<emailSetting>ErrorAndWarning</emailSetting>" +
                    "<inDesignOptions><process>None</process></inDesignOptions>" +
                    "<unsharpMaskOptions><amount>1.75</amount><radius>0.2</radius><threshold>2</threshold><monochrome>0</monochrome></unsharpMaskOptions>" +
                    "<unCompressOptions><process>None</process></unCompressOptions>" +
                    "</uploadParams>" +
                    "</uploadPostParam>\"";

    private final static String SUBMIT_QUERY = "Submit Query";

    /**
     * 上传文件到Scene7 Server
     *
     * @param destFolder 上传文件目录名称
     * @param file       上传文件
     */
    public static void uploadImageFile(String destFolder, File file) {
        if (StringUtils.isEmpty(destFolder)) {
            throw new RuntimeException("destFolder is null.");
        }
        if (file == null) {
            throw new RuntimeException("file is null.");
        }
        if (!file.exists()) {
            throw new RuntimeException(String.format("%s file not found.", file.getName()));
        }
        try {
            uploadImageFile(destFolder, file.getName(), new FileInputStream(file), true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件到Scene7 Server
     *
     * @param destFolder  上传文件目录名称
     * @param fileName    上传文件名
     * @param inputStream 上传文件流
     */
    public static void uploadImageFile(String destFolder, String fileName, InputStream inputStream) {
        uploadImageFile(destFolder, fileName, inputStream, true);
    }

    /**
     * 上传文件到Scene7 Server
     *
     * @param destFolder    上传文件目录名称
     * @param fileName      上传文件名
     * @param inputStream   上传文件流
     * @param isCloseStream 是否关闭文件流
     */
    public static void uploadImageFile(String destFolder, String fileName, InputStream inputStream, boolean isCloseStream) {
        if (StringUtils.isEmpty(destFolder)) {
            throw new RuntimeException("destFolder is null.");
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("fileName is null.");
        }
        if (inputStream == null) {
            throw new RuntimeException("inputStream is null");
        }
        uploadImageFileToS7(destFolder, fileName, inputStream, isCloseStream);
    }

    /**
     * 上传文件到Scene7 Server
     *
     * @param destFolder    上传文件目录名称
     * @param fileName      上传文件名
     * @param inputStream   上传文件流
     * @param isCloseStream 是否关闭文件流
     */
    private static void uploadImageFileToS7(String destFolder, String fileName, InputStream inputStream, boolean isCloseStream) {
        String url = Codes.getCodeName(S7HTTP_CONFIG, "Url");
        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("send Url not found.");
        }
        HttpPost post = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody(NAME_UPLOAD, SUBMIT_QUERY, ContentType.TEXT_PLAIN);
        builder.addTextBody(NAME_FILENAME, fileName, ContentType.TEXT_PLAIN);

        String userName = Codes.getCodeName(S7HTTP_CONFIG, "UserName");
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("send UserName not found.");
        }
        String password = Codes.getCodeName(S7HTTP_CONFIG, "Password");
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("send Password not found.");
        }
        builder.addTextBody(NAME_AUTH, String.format(VALUE_AUTH, userName, password), ContentType.TEXT_PLAIN);

        //uploadParams
        String dataNow = DateTimeUtil.getNow(DATE_PATTERN);
        String path = PATH_PRE;
        String asserts = destFolder;
        if (!StringUtils.isEmpty(asserts)) {
            if (asserts.startsWith("/")) {
                asserts = asserts.substring(1);
            }
            if (!asserts.endsWith("/")) {
                asserts += "/";
            }
            path += asserts;
        }
        String uploadParams = String.format(VALUE_UPLOAD_PARAMS, dataNow, path, path, fileName);
        builder.addTextBody(NAME_UPLOADPARAMS, uploadParams, ContentType.TEXT_PLAIN);

        //file
        builder.addPart(NAME_FILE, new InputStreamBody(inputStream, fileName));

        //build entity
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        // send https
        HttpClient httpclient = HttpClients.createDefault();
        try {
            HttpResponse response = httpclient.execute(post);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entitys = response.getEntity();
                if (entity != null) {
                    String message = EntityUtils.toString(entitys).replace("\n", "");
                    String resultMsg = String.format("[file:%s,size:%s,message%s]", fileName, entity.getContentLength(), message);
                    if (message.indexOf("<jobHandle>") > 0) {
                        logger.info("uploadImageFileToS7 OK " + resultMsg);
                    } else {
                        throw new RuntimeException("uploadImageFileToS7 error " + resultMsg);
                    }
                }
            } else {
                throw new RuntimeException(response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (isCloseStream) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

    }
}
