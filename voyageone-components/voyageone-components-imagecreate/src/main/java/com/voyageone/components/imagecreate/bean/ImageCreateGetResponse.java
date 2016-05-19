package com.voyageone.components.imagecreate.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.service.bean.openapi.image.GetImageResultBean;

import java.io.InputStream;

/**
 * @author chuanyu.liang 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ImageCreateGetResponse extends GetImageResultBean {

    @JsonIgnore
    private InputStream imageInputStream;

    public InputStream getImageInputStream() {
        return imageInputStream;
    }

    public void setImageInputStream(InputStream imageInputStream) {
        this.imageInputStream = imageInputStream;
    }
}
