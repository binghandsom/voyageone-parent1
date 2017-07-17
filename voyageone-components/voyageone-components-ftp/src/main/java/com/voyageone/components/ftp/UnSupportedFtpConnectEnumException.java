package com.voyageone.components.ftp;

public class UnSupportedFtpConnectEnumException extends RuntimeException {
    UnSupportedFtpConnectEnumException(FtpConstants.FtpConnectEnum connectEnum) {
        super("不支持的服务目标：" + connectEnum);
    }
}
