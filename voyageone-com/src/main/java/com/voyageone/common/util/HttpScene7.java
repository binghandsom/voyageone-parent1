package com.voyageone.common.util;

import com.voyageone.common.configs.Codes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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

    private final static String VALUE_NO_CDNAVLUD =
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "  <SOAP-ENV:Header>\n" +
                    "    <authHeader xmlns=\"http://www.scene7.com/IpsApi/xsd/2016-01-14-beta\">\n" +
                    "      <user>admin@voyageone.com</user>\n" +
                    "      <password>2016Dirzh#1</password>\n" +
                    "      <locale>en-US</locale>\n" +
                    "      <appName>Adobe.Scene7.SPS</appName>\n" +
                    "      <appVersion>6.10-194940</appVersion>\n" +
                    "      <faultHttpStatusCode>200</faultHttpStatusCode>\n" +
                    "    </authHeader>\n" +
                    "  </SOAP-ENV:Header>\n" +
                    "  <SOAP-ENV:Body>\n" +
                    "    <cdnCacheInvalidationParam xmlns=\"http://www.scene7.com/IpsApi/xsd/2016-01-14-beta\">\n" +
                    "      <companyHandle>c|8553</companyHandle>\n" +
                    "      <urlArray>\n" +
                    "        <items>%s</items>\n" +
                    "      </urlArray>\n" +
                    "    </cdnCacheInvalidationParam>\n" +
                    "  </SOAP-ENV:Body>\n" +
                    "</SOAP-ENV:Envelope>";

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
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    String message = EntityUtils.toString(httpEntity).replace("\n", "");
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

    /**
     * 清除 Scene7 Server Image CND Cache
     *
     * @param url 清除的URL
     */
    public static void cleanImageClearCDN(String url) {
        String serverUrl = "https://s7sps1apissl.scene7.com/scene7/services/IpsApiService";//Codes.getCodeName(S7HTTP_CONFIG, "Url");
        if (StringUtils.isEmpty(serverUrl)) {
            throw new RuntimeException("send serverUrl not found.");
        }

        if (StringUtils.isEmpty(url)) {
            throw new RuntimeException("send url not found.");
        }

        HttpPost post = new HttpPost(serverUrl);

        String userName = Codes.getCodeName(S7HTTP_CONFIG, "UserName");
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("send UserName not found.");
        }
        String password = Codes.getCodeName(S7HTTP_CONFIG, "Password");
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("send Password not found.");
        }
        String urlEncoded = String.format(VALUE_NO_CDNAVLUD, encodeString(url));
        StringEntity entity = new StringEntity(urlEncoded, "UTF-8");

        post.addHeader("Content-Type", "text/xml");
        post.addHeader("Pragma", "no-cache");
        post.addHeader("SOAPAction", "\"cdnCacheInvalidation\"");

        post.setEntity(entity);

        // send https
        HttpClient httpclient = HttpClients.createDefault();
        try {
            HttpResponse response = httpclient.execute(post);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                logger.info("cleanImageClearFromS7 OK " + url);
            } else {
                throw new RuntimeException(response.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 替换字符串中特殊字符
     */
    private static String encodeString(String strData) {
        if (strData == null) {
            return "";
        }
        strData = replaceString(strData, "&", "&amp;");
        strData = replaceString(strData, "<", "&lt;");
        strData = replaceString(strData, ">", "&gt;");
        strData = replaceString(strData, "&apos;", "&apos;");
        strData = replaceString(strData, "\"", "&quot;");
        return strData;
    }

    public static String replaceString(String strData, String regex, String replacement) {
        if (strData == null) {
            return null;
        }
        int index;
        index = strData.indexOf(regex);
        String strNew = "";
        if (index >= 0) {
            while (index >= 0) {
                strNew += strData.substring(0, index) + replacement;
                strData = strData.substring(index + regex.length());
                index = strData.indexOf(regex);
            }
            strNew += strData;
            return strNew;
        }
        return strData;
    }
}
