package com.voyageone.service.bean.openapi.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.openapi.OpenApiException;

/**
 * Created by dell on 2016/4/26.
 */
public class CreateImageParameter {
    protected String channelId;//
    protected int templateId;
    protected String file;
    protected String[] vParam;
    protected boolean isUploadUsCdn = false;

    public boolean isUploadUsCdn() {
        return isUploadUsCdn;
    }

    public void setUploadUsCdn(boolean uploadUsCdn) {
        isUploadUsCdn = uploadUsCdn;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String[] getVParam() {
        return vParam;
    }

    public void setVParam(String[] vParam) {
        this.vParam = vParam;
    }

    @JsonIgnore
    public String getVParamStr() {
        if (vParam == null) {
            vParam = new String[0];
        }
        return JacksonUtil.bean2Json(vParam);
    }

    public void checkInputValue() throws OpenApiException {
        if (StringUtil.isEmpty(channelId)) {
            throw new OpenApiException(ImageErrorEnum.ChannelIdNotNull);
        }
        if (templateId == 0) {
            throw new OpenApiException(ImageErrorEnum.ImageTemplateNotNull);
        }
        if (StringUtil.isEmpty(file)) {
            throw new OpenApiException(ImageErrorEnum.FileNotNull);
        }
        if (vParam == null) {
            throw new OpenApiException(ImageErrorEnum.VParamNotNull);
        }
    }
}
