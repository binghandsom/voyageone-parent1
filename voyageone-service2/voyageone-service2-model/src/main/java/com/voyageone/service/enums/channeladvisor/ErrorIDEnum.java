package com.voyageone.service.enums.channeladvisor;

import com.voyageone.common.util.StringUtils;

import java.util.Arrays;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public enum ErrorIDEnum {

    Info(1000, ""),

    Warning(2000, ""),

    SystemFailure(3000, ""),

    SystemUnavailable(3001, "The API is currently down for maintenance."),

    RateLimitExceeded(3002, "Rate limit exceeded. Please throttle your requests."),

    AuthorizationFailure(4000, ""),

    InvalidToken(4001, "Your seller token is invalid. Please contact our support team to obtain a valid authentication token."),

    InvalidSellerID(4002, "Your seller ID is invalid. Please contact our support team to obtain a valid seller ID."),

    ProductNotFound(5000, ""),

    ProductMissingRequiredFields(5001, ""),

    ProductFailedDataValidation(5002, ""),

    ProductFailedCreate(5003, ""),

    ProductFailedUpdate(5004, ""),

    ProductMissingCategory(5100, ""),

    ProductInvalidCategory(5101, ""),

    ProductPendingReview(5200, ""),

    OrderNotFound(6000, "order id not found."),

    InvalidOrder(6001, ""),

    InvalidOrderStatus(6002, ""),

    ShipmentFailed(6100, ""),

    InvalidTrackingNumber(6101, ""),

    InvalidShippingCarrier(6102, ""),

    InvalidShippingClass(6103, ""),

    RefundFailed(6200, ""),

    UnsupportedRefundReason(6201, ""),

    CancellationFailed(6300, ""),

    UnsupportedCancellationReason(6301, ""),

    InvalidRequest(7000, ""),

    MissingRequiredParameter(7001, ""),

    InvalidRequiredParameter(7002, ""),

    BatchNotFound(8000, "");

    private int code;
    private String defaultMessage;

    ErrorIDEnum(int code, String message) {
        this.code = code;
        this.defaultMessage = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getDefaultMessage() {
        return this.defaultMessage;
    }


    /**
     * 提供一个获取枚举的方法，能够根据数字获取枚举
     *
     * @param code code
     * @return 枚举
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static ErrorIDEnum getInstance(int code) {
        return Arrays.stream(ErrorIDEnum.values()).filter(e -> e.code == code).findFirst().get();
    }

    /**
     * 提供一个获取枚举的方法，能够根据数字或者字符串获取枚举
     *
     * @param value value
     * @return 枚举
     */
    public static ErrorIDEnum getInstance(String value) {
        return StringUtils.isDigit(value) ? getInstance(Integer.parseInt(value)) : null;
    }
}
