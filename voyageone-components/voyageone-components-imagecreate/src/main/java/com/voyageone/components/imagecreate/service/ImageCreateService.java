package com.voyageone.components.imagecreate.service;

import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListResponse;
import com.voyageone.components.imagecreate.bean.ImageCreateGetRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateGetResponse;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.OpenApiResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * ImageCreateService
 *
 * @author chuanyu.liang, 12/5/16.
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ImageCreateService extends ComponentBase {

    /**
     * 单个图片生成
     *
     * @param request 请求体
     * @return ImageCreateGetResponse
     */
    public ImageCreateGetResponse getImage(ImageCreateGetRequest request) throws Exception {
        if (request == null) {
            throw new OpenApiException(ImageErrorEnum.ParametersRequired);
        }

        request.checkInputValue();

        ImageCreateGetResponse response = sendData(getProstUrl() + "get", request.beanToUrl(), null, ImageCreateGetResponse.class);

        if (request.isFillStream()) {
            // build imageURL
            String imageUrl = getOssHttpURL(response.getResultData().getFilePath());
            response.setImageInputStream(getFileStream(imageUrl));
        }
        return response;
    }

    /**
     * 多个图片生成
     *
     * @param request 请求体
     * @return ImageCreateGetResponse
     */
    public ImageCreateAddListResponse addList(ImageCreateAddListRequest request) throws Exception {
        if (request == null || request.getData() == null || request.getData().isEmpty()) {
            throw new OpenApiException(ImageErrorEnum.ParametersRequired);
        }

        for (CreateImageParameter createImageParameter : request.getData()) {
            createImageParameter.checkInputValue();
        }

        return sendData(getProstUrl() + "addList", JacksonUtil.bean2Json(request), "application/json", ImageCreateAddListResponse.class);
    }

    private <E> E sendData(String url, String param, String accept, Class<E> clazz) throws Exception {
        String result;
        if (accept == null) {
            result = HttpUtils.post(url, param);
        } else {
            result = HttpUtils.post(url, param, "application/json", null);
        }
        Map responseMap = JacksonUtil.json2Bean(result, HashMap.class);
        if (responseMap.containsKey("message")) {
            throw new OpenApiException(10200, (String) responseMap.get("message"));
        }

        E response = JacksonUtil.json2Bean(result, clazz);
        if (response == null) {
            throw new OpenApiException(ImageErrorEnum.SystemError);
        }
        OpenApiResultBean openApiResultBean = (OpenApiResultBean) response;
        if (openApiResultBean.getErrorCode() != 0) {
            throw new OpenApiException(openApiResultBean.getErrorCode(), openApiResultBean.getErrorMsg());
        }
        return response;
    }


    /**
     * 下载文件OutputStream
     */
    private InputStream getFileStream(String urlString) throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            con.setRequestProperty("Accept-Charset", "UTF-8");
            //设置请求超时为5s
            con.setConnectTimeout(10 * 1000);
            //设置请求超时为5s
            con.setReadTimeout(20 * 1000);
            // 输入流
            is = con.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);
            return new ByteArrayInputStream(bytes);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String getProstUrl() {
        return "http://" + Codes.getCodeName("VO_IMAGE_SERVER", "DOMAIN") + "/createservice/";
    }

    public String getOssHttpURL(String filePath) {
        String imageUrl = Codes.getCodeName("AliYun_OSS_Confige", "url");
        if (!imageUrl.endsWith("/")) {
            imageUrl += "/";
        }
        imageUrl += filePath;
        return imageUrl;
    }
}
