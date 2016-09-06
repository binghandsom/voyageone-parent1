package com.voyageone.web2.sdk.api.channeladvisor.enums;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum ErrorIDEnum {

    Info(1000),

    Warning(2000),

    SystemFailure(3000),

    SystemUnavailable(3001),

    RateLimitExceeded(3002),

    AuthorizationFailure(4000),

    InvalidToken(4001),

    InvalidSellerID(4002),

    ProductNotFound(5000),

    ProductMissingRequiredFields(5001),

    ProductFailedDataValidation(5002),

    ProductFailedCreate(5003),

    ProductFailedUpdate(5004),

    ProductMissingCategory(5100),

    ProductInvalidCategory(5101),

    ProductPendingReview(5200),

    OrderNotFound(6000),

    InvalidOrder(6001),

    InvalidOrderStatus(6002),

    ShipmentFailed(6100),

    InvalidTrackingNumber(6101),

    InvalidShippingCarrier(6102),

    InvalidShippingClass(6103),

    RefundFailed(6200),

    UnsupportedRefundReason(6201),

    CancellationFailed(6300),

    UnsupportedCancellationReason(6301),

    InvalidRequest(7000),

    MissingRequiredParameter(7001),

    InvalidRequiredParameter(7002),

    BatchNotFound(8000);

    private int code;

    ErrorIDEnum(int code) {
        this.code = code;
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static ErrorIDEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? Arrays.asList(ErrorIDEnum.values()).stream().filter(e -> e.code == Integer.parseInt(value)).findFirst().get() : ErrorIDEnum.valueOf(value);
    }
}
