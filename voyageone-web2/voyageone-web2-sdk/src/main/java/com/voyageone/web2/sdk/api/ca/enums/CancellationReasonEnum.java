package com.voyageone.web2.sdk.api.ca.enums;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum CancellationReasonEnum {

    Other(1),
    GeneralAdjustment(100),
    ItemNotAvailable(101),
    CustomerReturnedItem(102),
    CouldNotShip(103),
    AlternateItemProvided(104),
    BuyerCanceled(105),
    CustomerExchange(106),
    MerchandiseNotReceived(107),
    ShippingAddressUndeliverable(108);

    private int code;

    CancellationReasonEnum(int code) {
        this.code = code;
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static CancellationReasonEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? Arrays.asList(CancellationReasonEnum.values()).stream().filter(e -> e.code == Integer.parseInt(value)).findFirst().get() : CancellationReasonEnum.valueOf(value);
    }

}
