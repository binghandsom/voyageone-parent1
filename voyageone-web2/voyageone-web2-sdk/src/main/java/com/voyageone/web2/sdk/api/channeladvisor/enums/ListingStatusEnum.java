package com.voyageone.web2.sdk.api.channeladvisor.enums;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum ListingStatusEnum {

    Live(0),
    NotLive(1),
    Pending(2);

    private int code;

    ListingStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字获取枚举
     *
     * @param code code
     * @return 枚举
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static ListingStatusEnum getInstance(int code) {
        return Arrays.stream(ListingStatusEnum.values()).filter(e -> e.code == code).findFirst().get();
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static ListingStatusEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? getInstance(Integer.parseInt(value)) : null;
    }
}
