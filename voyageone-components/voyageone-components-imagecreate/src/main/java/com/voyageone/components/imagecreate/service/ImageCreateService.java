package com.voyageone.components.imagecreate.service;

import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateAddListResponse;
import com.voyageone.components.imagecreate.bean.ImageCreateGetRequest;
import com.voyageone.components.imagecreate.bean.ImageCreateGetResponse;
import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.OpenApiResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * ImageCreateService
 *
 * @author chuanyu.liang, 12/5/16.
 * @version 2.0.1
 * @since 2.0.0
 */
public class ImageCreateService {

    //private static final String POST_URL = "http://localhost:8080/rest/product/image/";
    private static final String POST_URL = "http://image.voyageone.com.cn/createservice/";

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

        return sendData(POST_URL + "get", request.beanToUrl(), null, ImageCreateGetResponse.class);
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

        return sendData(POST_URL + "addList", JacksonUtil.bean2Json(request), "application/json", ImageCreateAddListResponse.class);
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
        OpenApiResultBean openApiResultBean = ((OpenApiResultBean) response);
        if (openApiResultBean.getErrorCode() != 0) {
            throw new OpenApiException(openApiResultBean.getErrorCode(), openApiResultBean.getErrorMsg());
        }
        return response;
    }
}
