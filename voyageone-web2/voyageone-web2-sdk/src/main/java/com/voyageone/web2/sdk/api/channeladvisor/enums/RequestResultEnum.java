package com.voyageone.web2.sdk.api.channeladvisor.enums;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum RequestResultEnum {

    Success(0),
    Fail(1);

    private int code;

    RequestResultEnum(int code) {
        this.code = code;
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static RequestResultEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? Arrays.asList(RequestResultEnum.values()).stream().filter(e -> e.code == Integer.parseInt(value)).findFirst().get() : RequestResultEnum.valueOf(value);
    }
}
