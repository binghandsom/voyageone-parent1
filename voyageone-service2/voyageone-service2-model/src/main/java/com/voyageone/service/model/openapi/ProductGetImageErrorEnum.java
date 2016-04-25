package com.voyageone.service.model.openapi;

public enum  ProductGetImageErrorEnum implements ErrorEnumInterface {
    ImageTemplateNotNull(10101, "Image template must not be null"),
    ChannelIdNotNull(10102, "ChannelId must not be null"),
    FileNotNull(10103, "File  must not be null"),
    VParamNotNull(10104, "VParam template must not be null"),
    LiquidCreateImageError(10201, "Call  liquid interface exception"),
    AliyunOSSUploadError(10202, "Call  aliyun OSS interface exception"),
    SystemError(10200, " System exception");
    int code;
    String msg;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    ProductGetImageErrorEnum(int code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }
}
