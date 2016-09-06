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

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static ListingStatusEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? Arrays.asList(ListingStatusEnum.values()).stream().filter(e -> e.code == Integer.parseInt(value)).findFirst().get() : ListingStatusEnum.valueOf(value);
    }
}
