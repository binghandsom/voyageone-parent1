package com.voyageone.service.model.cms.mongo.channel;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * {@link CmsBtImageGroupModel_Image} 的图片管理Model
 * @author jeff.duan, 16/5/5
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtImageGroupModel_Image {

    private String originUrl;
    private String platformUrl;
    private Long platformImageId;
    private Integer status;
    private String errorMsg;
    private String statusName;

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getPlatformUrl() {
        return platformUrl;
    }

    public void setPlatformUrl(String platformUrl) {
        this.platformUrl = platformUrl;
    }

    public Long getPlatformImageId() {
        return platformImageId;
    }

    public void setPlatformImageId(Long platformImageId) {
        this.platformImageId = platformImageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}