package com.voyageone.components.imagecreate.bean;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;

import java.io.UnsupportedEncodingException;

/**
 * @author chuanyu.liang 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ImageCreateGetRequest extends CreateImageParameter {

    public String beanToUrl() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("cId=").append(channelId);
        sb.append("&templateId=").append(templateId);
        sb.append("&file=").append(file);
        sb.append("&vparam=").append(JacksonUtil.bean2Json(vParam));
        return sb.toString();
    }

}
