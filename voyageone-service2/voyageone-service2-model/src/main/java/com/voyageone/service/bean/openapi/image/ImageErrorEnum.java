package com.voyageone.service.bean.openapi.image;

import com.voyageone.service.bean.openapi.ErrorEnumInterface;

public enum ImageErrorEnum implements ErrorEnumInterface {
    ParametersRequired(10100, "The parameters required"),
    ImageTemplateNotNull(10101, "Image template must not be null"),
    ChannelIdNotNull(10102, "ChannelId must not be null"),
    FileNotNull(10103, "File  must not be null"),
    VParamNotNull(10104, "VParam template must not be null"),
    TASKIDNotNull(10105, "taskId not found."),
    TASKNotNull(10106, "task not found."),
    LiquidCreateImageError(10201, "Call  liquid interface exception"),
    LiquidCreateImageExceptionImage(1020101, "liquid create an exception image"),
    LiquidCreateImageNullImage(1020102,"liquid create a null image"),
    AliyunOSSUploadError(10202, "Call  aliyun OSS interface exception"),
    USCDNUploadError(10203, "Call  US CDN interface exception"),
    ParametersOutSize(10204, "parameter max size is ."),

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

    ImageErrorEnum(int code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }
}
