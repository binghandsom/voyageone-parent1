package com.voyageone.service.bean.openapi.image;

/**
 * Created by dell on 2016/4/26.
 */
public class CreateImageParameter {
    String channelId;//
    int templateId;
    String file;
    String vParam;
    boolean isUploadUsCdn;

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
    public String getVParam() {
        return vParam;
    }
    public void setVParam(String vParam) {
        this.vParam = vParam;
    }
}
