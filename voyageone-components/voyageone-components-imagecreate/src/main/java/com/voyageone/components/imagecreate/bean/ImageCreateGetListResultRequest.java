package com.voyageone.components.imagecreate.bean;

import com.voyageone.service.bean.openapi.OpenApiException;
import com.voyageone.service.bean.openapi.image.ImageErrorEnum;

/**
 * @author chuanyu.liang 2016/5/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ImageCreateGetListResultRequest {

    protected Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void checkInputValue() throws OpenApiException {
        if (taskId == null) {
            throw new OpenApiException(ImageErrorEnum.TASKIDNotNull);
        }
    }
}
